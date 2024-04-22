package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ManagementConverterTests {
    private ManagementConverter managementConverter;
    @MockBean
    private MemberConverter memberConverter;
    @Autowired
    @MockBean
    private DepartmentRepository departmentRepository;
    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        managementConverter = new ManagementConverter();

        Field field = ManagementConverter.class.getDeclaredField("departmentRepository");
        field.setAccessible(true);
        field.set(managementConverter, departmentRepository);

        Field fieldMC = ManagementConverter.class.getDeclaredField("memberConverter");
        fieldMC.setAccessible(true);
        fieldMC.set(managementConverter, memberConverter);


    }
    @Test
    @DisplayName("Test converting Management entity to ManagementDto")
    public void toDtoTest(){
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Department department = new Department(1L,"Department1");

        Member member = new Member(1L,"Lucy","Parker",academicTitle, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(1L,"Lucy","Parker",academicTitle.getTitle(),
                educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        Management management = new Management(1L, department, member,
                "handler", LocalDate.of(2023,4,4),LocalDate.of(2024,5,5));

        when(memberConverter.toDto(member)).thenReturn(memberDto);

        ManagementDto managementDto = managementConverter.toDto(management);

        assertEquals(management.getId(), managementDto.getId());
        assertEquals(management.getRole(), managementDto.getRole());
        assertEquals(management.getStartDate(), managementDto.getStartDate());
        assertEquals(management.getEndDate(), managementDto.getEndDate());
    }
    @Test
    @DisplayName("Test converting ManagementDto to Management entity")
    public void toEntityTest (){
        Department department = new Department(2L,"Department2");
        AcademicTitle academicTitle = new AcademicTitle(2L,"AT2");
        EducationTitle educationTitle = new EducationTitle(2L,"ET2");
        ScientificField scientificField = new ScientificField(2L,"ScF2");
        MemberDto memberDto = new MemberDto(2L,"Lucy", "Parker", academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member = new Member(2L,"Lucy","Parker",academicTitle, educationTitle, scientificField, department);

        ManagementDto managementDto = new ManagementDto(2L,department.getName(),memberDto,"handler",LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        when(departmentRepository.findByNameIgnoreCase(managementDto.getDepartmentDto())).thenReturn(Optional.of(department));
        when(memberConverter.toEntity(managementDto.getMemberDto())).thenReturn(member);

        Management management = managementConverter.toEntity(managementDto);

        assertEquals(managementDto.getId(), management.getId());
        assertEquals(managementDto.getRole(), management.getRole());
        assertEquals(managementDto.getStartDate(), management.getStartDate());
        assertEquals(managementDto.getEndDate(), management.getEndDate());
    }
}
