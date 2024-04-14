package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class DepartmentRepositoryTests {
    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    public void findByNameIgnoreCaseTest(){
        Department department = departmentRepository.save(new Department(1L,"Department1"));
        assertNotNull(department);
        Optional<Department> dep = departmentRepository.findByNameIgnoreCase(department.getName());
        assertTrue(dep.isPresent());
        assertEquals(department,dep.get());
    }
    @Test
    public void findMaxIdTest(){
        Department department1 = departmentRepository.save(new Department(1L,"MAX1"));
        Department department2 = departmentRepository.save(new Department(2L,"MAX2"));
        Department department3 = departmentRepository.save(new Department(3L,"MAX3"));
        assertNotNull(department1);
        assertNotNull(department2);
        assertNotNull(department3);
        Long maxID = departmentRepository.findMaxId();
        assertEquals(department3.getId(),maxID);
    }
}
