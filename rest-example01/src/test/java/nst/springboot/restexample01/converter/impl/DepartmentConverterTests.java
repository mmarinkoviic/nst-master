package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.dto.DepartmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DepartmentConverterTests {
    private DepartmentConverter departmentConverter;
    @BeforeEach
    public void setUp(){
        departmentConverter = new DepartmentConverter();
    }

    @Test
    @DisplayName("Test converting Department entity to DepartmentDto")
    public void toDtoTest(){
        Department department = new Department(1L,"Department1");
        DepartmentDto departmentDto = departmentConverter.toDto(department);

        assertEquals(department.getId(), departmentDto.getId());
        assertEquals(department.getName(), departmentDto.getName());
    }
    @Test
    @DisplayName("Test converting DepartmentDto to Department entity")
    public void toEntityTest (){
        DepartmentDto departmentDto = new DepartmentDto(2L,"Department2");
        Department department = departmentConverter.toEntity(departmentDto);

        assertEquals(departmentDto.getId(), department.getId());
        assertEquals(departmentDto.getName(), department.getName());
    }

}
