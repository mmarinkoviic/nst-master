package nst.springboot.restexample01.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.domain.Subject;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.SubjectRepository;
import nst.springboot.restexample01.service.SubjectService;
import nst.springboot.restexample01.converter.impl.SubjectConverter;
import nst.springboot.restexample01.dto.SubjectDto;
import org.springframework.stereotype.Service;
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

        Optional<Subject> subject = subjectRepository.findByNameIgnoreCase(nameSubject);
        if(subject.isPresent()){
            throw new EntityExistsException("Subject already exist!");
        }else{
            Department dep = departmentRepository.findByNameIgnoreCase(nameDepartment)
                    .orElseThrow(() -> new EntityNotFoundException("Department with name " + nameDepartment + " not found."));
            Long id = 0L;
            if(subjectRepository.findMaxId() != null){
                id = subjectRepository.findMaxId();
            }
            Subject subj = new Subject(id +1,nameSubject,espb,dep);
            subjectRepository.save(subj);
            return subjectConverter.toDto(subj);
        }
    }

    @Override
    public List<SubjectDto> getAll() {
        return subjectRepository
                .findAll()
                .stream().map(entity -> subjectConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject subj = subject.get();
            subjectRepository.delete(subj);
        } else {
            throw new EntityNotFoundException("Subject does not exist!");
        }

    }

    @Override
    @Transactional
    public void update(SubjectDto subjectDto) throws Exception {
        Long id = subjectDto.getId();
        if(subjectRepository.findByNameIgnoreCase(subjectDto.getName()).isPresent()){throw new EntityExistsException("Subject "+ subjectDto.getName()+" already exist!");}
        if (id == null || !subjectRepository.existsById(id)){
            throw new EntityNotFoundException("Subject with id " + id + " not found.");
        }
        Subject subject = subjectRepository.findById(id).get();

        subject.setName(subjectDto.getName());
        subject.setEsbp(subjectDto.getEsbp());

        Department department = departmentRepository.findByNameIgnoreCase(subjectDto.getDepartment()).orElseThrow(()->new EntityNotFoundException("Department with name " + subjectDto.getDepartment() + " not found."));

        subject.setDepartment(department);
        subjectRepository.save(subject);
    }

    @Override
    public SubjectDto findById(Long id) throws Exception {
        Subject subject = subjectRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Subject does not exist!"));
        return subjectConverter.toDto(subject);

    }

    @Override
    public List<SubjectDto> findByDepartment(Long id) throws Exception{
        Department department = departmentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Department with id " + id + " does not exist!"));
        List<Subject> subjectsList = subjectRepository.findByDepartmentId(id);
        if(subjectsList.isEmpty()){
            throw new EntityNotFoundException("There are no subject in department " + department.getName() + ".");
        }
        return subjectsList.stream().map(entity -> subjectConverter.toDto(entity))
                .collect(Collectors.toList());
    }


}
