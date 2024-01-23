package nst.springboot.restexample01.controller.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nst.springboot.restexample01.controller.domain.Department;
import nst.springboot.restexample01.controller.domain.Management;
import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.domain.Subject;
import nst.springboot.restexample01.controller.repository.DepartmentRepository;
import nst.springboot.restexample01.controller.repository.ManagementRepository;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.repository.SubjectRepository;
import nst.springboot.restexample01.controller.service.DepartmentService;
import nst.springboot.restexample01.converter.impl.DepartmentConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.converter.impl.SubjectConverter;
import nst.springboot.restexample01.dto.DepartmentDto;
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

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentConverter departmentConverter,
            SubjectRepository subjectRepository,
            MemberRepository memberRepository,
            ManagementRepository managementRepository,
            MemberConverter memberConverter,
            SubjectConverter subjectConverter) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
        this.subjectRepository = subjectRepository;
        this.memberRepository = memberRepository;
        this.managementRepository = managementRepository;
        this.memberConverter = memberConverter;
        this.subjectConverter = subjectConverter;
    }

    @Override
    @Transactional
    public DepartmentDto save(String name) throws Exception {
        Optional<Department> dept = departmentRepository.findByName(name);
        if (dept.isPresent()) {
            throw new Exception("Department already exist!");
        } else {
            Department department= new Department(departmentRepository.count()+1,name);
            department = departmentRepository.save(department);
            return departmentConverter.toDto(department);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Department department = departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        List<Subject> subjectList = subjectRepository.findByDepartmentName(department.getName());
        List<Member> memberList = memberRepository.findByDepartmentName(department.getName());
        if(subjectList.isEmpty() && memberList.isEmpty()){
            departmentRepository.delete(department);
        }else{
            throw new Exception("Department is not empty. In order to delete department, it must be empty!");
        }

    }

    @Override
    @Transactional
    public void update(Long id, String newName) throws Exception {
        if(departmentRepository.findByName(newName).isPresent()){throw new Exception("Department "+ newName+ " already exist!");}
        Department department = departmentRepository.findById(id).orElseThrow(()->new Exception("Department with id " + id +" not found."));
        department.setName(newName);
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDto findById(Long id) throws Exception {
        Department department = departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        return departmentConverter.toDto(department);
    }

    @Override
    @Transactional
    public void putSecretary(Long departmentId, Long memberId) throws Exception {
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new Exception("Department does not exist!"));
        Member member = memberRepository.findById(memberId).orElseThrow(()->new Exception("Member does not exist!"));

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
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new Exception("Department does not exist!"));
        Member member = memberRepository.findById(memberId).orElseThrow(()->new Exception("Member does not exist!"));

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
        departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        Management management = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(id,"secretary")
                .orElseThrow(()->new Exception("Currently, department does not have a handler!"));
        return memberConverter.toDto(management.getMember());
    }

    @Override
    public MemberDto getHandler(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        Management management = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(id,"handler")
                .orElseThrow(()->new Exception("Currently, department does not have a handler!"));
        return memberConverter.toDto(management.getMember());
    }

    @Override
    public List<SubjectDto> getSubjects(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        List<Subject> subjectList = subjectRepository.findByDepartmentId(id);
        if(subjectList.isEmpty()){throw new Exception("Department does not have subjects!");}
        return subjectList.stream().map(subject -> subjectConverter.toDto(subject)).collect(Collectors.toList());
    }

    @Override
    public List<MemberDto> getMembers(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        List<Member> memberList = memberRepository.findByDepartmentId(id);
        if(memberList.isEmpty()){throw new Exception("Department does not have members!");}
        return memberList.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());}

    @Override
    public List<DepartmentDto> getAll() {
        return departmentRepository
                .findAll()
                .stream().map(entity -> departmentConverter.toDto(entity))
                .collect(Collectors.toList());
    }

}
