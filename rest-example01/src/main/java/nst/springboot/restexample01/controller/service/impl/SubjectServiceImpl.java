package nst.springboot.restexample01.controller.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.controller.domain.Department;
import nst.springboot.restexample01.controller.domain.Subject;
import nst.springboot.restexample01.controller.repository.DepartmentRepository;
import nst.springboot.restexample01.controller.repository.SubjectRepository;
import nst.springboot.restexample01.controller.service.SubjectService;
import nst.springboot.restexample01.converter.impl.DepartmentConverter;
import nst.springboot.restexample01.converter.impl.SubjectConverter;
import nst.springboot.restexample01.dto.SubjectDto;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectConverter subjectConverter;
    private SubjectRepository subjectRepository;
    private DepartmentRepository departmentRepository;

    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            DepartmentRepository departmentRepository,
            SubjectConverter subjectConverter) {
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
        this.subjectConverter = subjectConverter;
    }

    @Override
    @Transactional
    public SubjectDto save(String nameSubject, int espb, String nameDepartment) throws Exception {

        Optional<Subject> subject = subjectRepository.findByName(nameSubject);
        if(subject.isPresent()){
            throw new Exception("Subject already exist!");
        }else{
            Department dep = departmentRepository.findByName(nameDepartment)
                    .orElseThrow(() -> new EntityNotFoundException("Department with name " + nameDepartment + " not found."));

            Subject subj = new Subject(subjectRepository.count()+1,nameSubject,espb,dep);
            subjectRepository.save(subj);
            return subjectConverter.toDto(subj);
        }
    }

    @Override
    public List<String> getAll() {
        return subjectRepository
                .findAll()
                .stream().map(entity -> subjectConverter.toDto(entity).toString()).sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject subj = subject.get();
            subjectRepository.delete(subj);
        } else {
            throw new Exception("Subject does not exist!");
        }

    }

    @Override
    public void delete(String nameSubject) throws Exception {
        Optional<Subject> subject = subjectRepository.findByName(nameSubject);
        if(subject.isPresent()){
            Subject subj = subject.get();
            subjectRepository.delete(subj);
        }else{
            throw new Exception("Subject does not exist!");
        }
    }

    @Override
    @Transactional
    public void update(SubjectDto subjectDto) throws Exception {
        Long id = subjectDto.getId();
        if (id == null || !subjectRepository.existsById(id)){
            throw new EntityNotFoundException("Subject with id " + id + " not found.");
        }
        Subject subject = subjectRepository.findById(id).get();

        subject.setName(subjectDto.getName());
        subject.setEsbp(subjectDto.getEsbp());

        Department department = departmentRepository.findByName(subjectDto.getDepartment()).orElseThrow(()->new Exception("Department with name " + subjectDto.getDepartment() + " not found."));

        subject.setDepartment(department);
        subjectRepository.save(subject);
    }

    @Override
    public String findById(Long id) throws Exception {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject subj = subject.get();
            return subjectConverter.toDto(subj).toString();
        } else {
            throw new Exception("Subject does not exist!");
        }
    }

    @Override
    public String findByName(String nameSubject) throws Exception {
        Optional<Subject> subject = subjectRepository.findByName(nameSubject);
        if(subject.isPresent()){
            return subjectConverter.toDto(subject.get()).toString();
        }else{
            throw new Exception("Subject does not exist!");
        }
    }

    @Override
    public List<String> findByDepartmentName(String department) throws Exception{
        departmentRepository.findByName(department).orElseThrow(()-> new Exception("Department " + department + " does not exist!"));
        List<Subject> subjectsList = subjectRepository.findByDepartmentName(department);
        if(subjectsList.isEmpty()){
            throw new Exception("There are no subject in department " + department + ".");
        }
        return subjectsList.stream().map(entity -> subjectConverter.toDto(entity).toString()).sorted()
                .collect(Collectors.toList());
    }


}
