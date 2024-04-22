package nst.springboot.restexample01.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.domain.Management;
import nst.springboot.restexample01.domain.Member;
import nst.springboot.restexample01.domain.Subject;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.ManagementRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.repository.SubjectRepository;
import nst.springboot.restexample01.service.DepartmentService;
import nst.springboot.restexample01.converter.impl.DepartmentConverter;
import nst.springboot.restexample01.converter.impl.ManagementConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.converter.impl.SubjectConverter;
import nst.springboot.restexample01.dto.DepartmentDto;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentConverter departmentConverter;
    private DepartmentRepository departmentRepository;
    private SubjectRepository subjectRepository;
    private MemberRepository memberRepository;
    private ManagementRepository managementRepository;
    private MemberConverter memberConverter;
    private SubjectConverter subjectConverter;
    private ManagementConverter managementConverter;

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentConverter departmentConverter,
            SubjectRepository subjectRepository,
            MemberRepository memberRepository,
            ManagementRepository managementRepository,
            MemberConverter memberConverter,
            SubjectConverter subjectConverter,
            ManagementConverter managementConverter) {

        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
        this.memberRepository = memberRepository;
        this.managementRepository = managementRepository;

        this.departmentConverter = departmentConverter;
        this.memberConverter = memberConverter;
        this.subjectConverter = subjectConverter;
        this.managementConverter = managementConverter;
    }

    @Override
    @Transactional
    public DepartmentDto save(String name) throws Exception {
        Optional<Department> dept = departmentRepository.findByNameIgnoreCase(name);
        if (dept.isPresent()) {
            throw new EntityExistsException("Department already exist!");
        } else {
            Department department= new Department(departmentRepository.findMaxId()+1,name);
            department = departmentRepository.save(department);
            return departmentConverter.toDto(department);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Department department = departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        List<Subject> subjectList = subjectRepository.findByDepartmentId(id);
        List<Member> memberList = memberRepository.findByDepartmentId(id);
        if(subjectList.isEmpty() && memberList.isEmpty()){
            departmentRepository.delete(department);
        }else{
            throw new Exception("Department is not empty. In order to delete department, it must be empty!");
        }

    }

    @Override
    @Transactional
    public void update(Long id, String newName) throws Exception {
        if(departmentRepository.findByNameIgnoreCase(newName).isPresent()){throw new EntityExistsException("Department "+ newName+ " already exist!");}
        Department department = departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department with id " + id +" not found."));
        department.setName(newName);
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDto findById(Long id) throws Exception {
        Department department = departmentRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        return departmentConverter.toDto(department);
    }

    @Override
    @Transactional
    public void putSecretary(Long departmentId, Long memberId) throws Exception {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new EntityNotFoundException("Member does not exist!"));

        Optional<Management> current = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId,"secretary");
        Optional<Management> handler = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId,"handler");
        if(current.isPresent()){
            if(current.get().getMember().getId().equals(memberId)){
                throw new Exception("This member is already secretary of department!");
            }
            Management old = current.get();
            old.setEndDate(LocalDate.now());
            managementRepository.save(old);
        }
        if(handler.isPresent()){
            if(handler.get().getMember().getId().equals(memberId)){
                throw new Exception("This member is already handler of department!");
            }
        }
        Management management = new Management(managementRepository.count()+1, department,member,"secretary", LocalDate.now(),null);
        managementRepository.save(management);

    }

    @Override
    public void putHandler(Long departmentId, Long memberId) throws Exception {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new EntityNotFoundException("Member does not exist!"));

        Optional<Management> current = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId,"handler");
        Optional<Management> secretary = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId,"secretary");
        if(current.isPresent()){
            if(current.get().getMember().getId().equals(memberId)){
                throw new Exception("This member is already handler of department!");
            }
            Management old = current.get();
            old.setEndDate(LocalDate.now());
            managementRepository.save(old);
        }
        if(secretary.isPresent()){
            if(secretary.get().getMember().getId().equals(memberId)){
                throw new Exception("This member is already secretary of department!");
            }
        }
        Management management = new Management(managementRepository.count()+1, department,member,"handler", LocalDate.now(),null);
        managementRepository.save(management);
    }

    @Override
    public MemberDto getSecretary(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        Management management = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(id,"secretary")
                .orElseThrow(()->new EntityNotFoundException("Currently, department does not have a secretary!"));
        return memberConverter.toDto(management.getMember());
    }

    @Override
    public MemberDto getHandler(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        Management management = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(id,"handler")
                .orElseThrow(()->new EntityNotFoundException("Currently, department does not have a handler!"));
        return memberConverter.toDto(management.getMember());
    }

    @Override
    public List<ManagementDto> getSecretaryHistory(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        List<Management> secretaryHistory = managementRepository.findByDepartmentIdAndRole(id,"secretary");
        if(secretaryHistory.isEmpty()){
            throw new EntityNotFoundException("There are no history!");
        }
        return secretaryHistory.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
    }

    @Override
    public List<ManagementDto> getHandlerHistory(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        List<Management> handlerHistory = managementRepository.findByDepartmentIdAndRole(id,"handler");
        if(handlerHistory.isEmpty()){
            throw new EntityNotFoundException("There are no history!");
        }
        return handlerHistory.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> getSubjects(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        List<Subject> subjectList = subjectRepository.findByDepartmentId(id);
        if(subjectList.isEmpty()){throw new EntityNotFoundException("Department does not have subjects!");}
        return subjectList.stream().map(subject -> subjectConverter.toDto(subject)).collect(Collectors.toList());
    }

    @Override
    public List<MemberDto> getMembers(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Department does not exist!"));
        List<Member> memberList = memberRepository.findByDepartmentId(id);
        if(memberList.isEmpty()){throw new EntityNotFoundException("Department does not have members!");}
        return memberList.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());}

    @Override
    public List<DepartmentDto> getAll() {
        return departmentRepository
                .findAll()
                .stream().map(entity -> departmentConverter.toDto(entity))
                .collect(Collectors.toList());
    }

}
