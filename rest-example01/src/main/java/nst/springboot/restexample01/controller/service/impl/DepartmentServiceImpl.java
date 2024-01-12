package nst.springboot.restexample01.controller.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.controller.domain.Department;
import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.domain.Subject;
import nst.springboot.restexample01.controller.repository.DepartmentRepository;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.repository.SubjectRepository;
import nst.springboot.restexample01.controller.service.DepartmentService;
import nst.springboot.restexample01.converter.impl.DepartmentConverter;
import nst.springboot.restexample01.dto.DepartmentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentConverter departmentConverter;
    private DepartmentRepository departmentRepository;
    private SubjectRepository subjectRepository;
    private MemberRepository memberRepository;

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentConverter departmentConverter,
            SubjectRepository subjectRepository,
            MemberRepository memberRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
        this.subjectRepository = subjectRepository;
        this.memberRepository = memberRepository;
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
    public void delete(String name) throws Exception{
        Department department = departmentRepository.findByName(name).orElseThrow(()->new Exception("Department does not exist!"));
        List<Subject> subjectList = subjectRepository.findByDepartmentName(name);
        List<Member> memberList = memberRepository.findByDepartmentName(name);
        if(subjectList.isEmpty() && memberList.isEmpty()){
            departmentRepository.delete(department);
        }else{
            throw new Exception("Department is not empty. In order to delete department, it must be empty!");
        }
    }

    @Override
    @Transactional
    public void update(DepartmentDto departmentDto) throws Exception {
        Long id = departmentDto.getId();
        departmentRepository.findById(id).orElseThrow(()->new Exception("Department with id " + id +" not found."));

        Department department = departmentRepository.findById(id).get();
        department.setName(departmentDto.getName());
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDto findById(Long id) throws Exception {
        Department department = departmentRepository.findById(id).orElseThrow(()->new Exception("Department does not exist!"));
        return departmentConverter.toDto(department);
    }

    @Override
    public List<String> getAll() {
        return departmentRepository
                .findAll()
                .stream().map(entity -> departmentConverter.toDto(entity).toString()).sorted()
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto findByName(String name) throws Exception {
        Department department = departmentRepository.findByName(name).orElseThrow(()->new Exception("Department does not exist!"));
        return departmentConverter.toDto(department);
    }
}
