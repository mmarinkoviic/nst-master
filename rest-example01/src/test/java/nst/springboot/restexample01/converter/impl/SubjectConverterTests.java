package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.domain.Subject;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest
public class SubjectConverterTests {
    private SubjectConverter subjectConverter;
    @Mock
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        subjectConverter = new SubjectConverter();

        departmentRepository = mock(DepartmentRepository.class);
        Field field = SubjectConverter.class.getDeclaredField("departmentRepository");
        field.setAccessible(true);
        field.set(subjectConverter, departmentRepository);
    }
    @Test
    @DisplayName("Test converting Subject entity to SubjectDto")
    public void toDtoTest(){
        Subject subject = new Subject(1L,"Subject1",6,new Department(1L,"Department1"));
        SubjectDto subjectDto = subjectConverter.toDto(subject);

        assertEquals(subject.getId(), subjectDto.getId());
        assertEquals(subject.getName(), subjectDto.getName());
        assertEquals(subject.getEsbp(), subjectDto.getEsbp());
        assertEquals(subject.getDepartment().getName(), subjectDto.getDepartment());
    }
    @Test
    @DisplayName("Test converting SubjectDto to Subject entity")
    public void toEntityTest (){

        Department department = new Department(2L,"Department2");
        SubjectDto subjectDto = new SubjectDto(2L, "Subject2", 6, department.getName());
        when(departmentRepository.findByNameIgnoreCase(subjectDto.getDepartment())).thenReturn(Optional.of(department));

        Subject subject = subjectConverter.toEntity(subjectDto);

        assertEquals(subjectDto.getId(), subject.getId());
        assertEquals(subjectDto.getName(), subject.getName());
        assertEquals(subjectDto.getEsbp(), subject.getEsbp());
        assertEquals(subjectDto.getDepartment(), subject.getDepartment().getName());
    }

}
