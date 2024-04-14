package nst.springboot.restexample01.repository;
import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.domain.EducationTitle;
import nst.springboot.restexample01.domain.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SubjectRepositoryTests {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void findByTitleIgnoreCaseTest(){
        Department department = new Department(1L,"department1");
        departmentRepository.save(department);
        Subject subject = subjectRepository.save(new Subject(1L,"subject1",6,department));
        assertNotNull(subject);
        Optional<Subject> subj = subjectRepository.findByNameIgnoreCase(subject.getName());
        assertTrue(subj.isPresent());
        assertEquals(subject,subj.get());
    }
    @Test
    public void findMaxIdTest(){
        Department department = new Department(1L,"Department");
        departmentRepository.save(department);
        Subject subject1 = subjectRepository.save(new Subject(1L,"MAX1",6,department));
        Subject subject2 = subjectRepository.save(new Subject(2L,"MAX2",6,department));
        Subject subject3 = subjectRepository.save(new Subject(3L,"MAX3",6,department));
        assertNotNull(subject1);
        assertNotNull(subject2);
        assertNotNull(subject3);
        Long maxID = subjectRepository.findMaxId();
        assertEquals(subject3.getId(),maxID);
    }
    @Test
    public void findByDepartmentIdTest(){
        Department department1 = new Department(1L,"Department1");
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        Subject subject1 = subjectRepository.save(new Subject(1L,"MAX1",6,department1));
        Subject subject2 = subjectRepository.save(new Subject(2L,"MAX2",6,department2));
        Subject subject3 = subjectRepository.save(new Subject(3L,"MAX3",6,department2));
        List<Subject> expectedList = Arrays.asList(subject2,subject3);

        List<Subject> returnedList = subjectRepository.findByDepartmentId(department2.getId());
        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }

}
