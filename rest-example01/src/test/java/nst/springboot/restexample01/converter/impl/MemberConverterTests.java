package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.repository.AcademicTitleRepository;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.EducationTitleRepository;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberConverterTests {
    private MemberConverter memberConverter;
    @Autowired
    @MockBean
    public EducationTitleRepository educationTitleRepository;
    @Autowired
    @MockBean
    public AcademicTitleRepository academicTitleRepository;
    @Autowired
    @MockBean
    public ScientificFieldRepository scientificFieldRepository;
    @Autowired
    @MockBean
    public DepartmentRepository departmentRepository;
    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        memberConverter = new MemberConverter();

        departmentRepository = mock(DepartmentRepository.class);
        Field fieldDepart = MemberConverter.class.getDeclaredField("departmentRepository");
        fieldDepart.setAccessible(true);
        fieldDepart.set(memberConverter, departmentRepository);
        educationTitleRepository = mock(EducationTitleRepository.class);
        Field fieldET = MemberConverter.class.getDeclaredField("educationTitleRepository");
        fieldET.setAccessible(true);
        fieldET.set(memberConverter, educationTitleRepository);
        academicTitleRepository = mock(AcademicTitleRepository.class);
        Field fieldAT = MemberConverter.class.getDeclaredField("academicTitleRepository");
        fieldAT.setAccessible(true);
        fieldAT.set(memberConverter, academicTitleRepository);
        scientificFieldRepository = mock(ScientificFieldRepository.class);
        Field fieldScF = MemberConverter.class.getDeclaredField("scientificFieldRepository");
        fieldScF.setAccessible(true);
        fieldScF.set(memberConverter, scientificFieldRepository);
    }

    @Test
    @DisplayName("Test converting Member entity to MemberDto")
    public void toDtoTest(){
        Member member = new Member(1L,"David","Becker",new AcademicTitle(1L,"AT1"),new EducationTitle(1L,"ET1"),new ScientificField(1L,"ScF1"),new Department(1L,"Department1"));
        MemberDto memberDto = memberConverter.toDto(member);

        assertEquals(member.getId(), memberDto.getId());
        assertEquals(member.getFirstName(), memberDto.getFirstName());
        assertEquals(member.getAcademicTitle().getTitle(), memberDto.getAcademicTitle());
        assertEquals(member.getEducationTitle().getTitle(), memberDto.getEducationTitle());
        assertEquals(member.getScientificField().getScfField(), memberDto.getScientificField());
        assertEquals(member.getDepartment().getName(), memberDto.getDepartment());
    }
    @Test
    @DisplayName("Test converting MemberDto to Member entity")
    public void toEntityTest (){
        Department department = new Department(2L,"Department2");
        EducationTitle educationTitle = new EducationTitle(2L,"ET2");
        AcademicTitle academicTitle = new AcademicTitle(2L, "AT2");
        ScientificField scientificField = new ScientificField(2L, "ScF2");

        MemberDto memberDto = new MemberDto(2L, "Derek","Grej",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        when(educationTitleRepository.findByTitleIgnoreCase(memberDto.getEducationTitle())).thenReturn(Optional.of(educationTitle));
        when(academicTitleRepository.findByTitleIgnoreCase(memberDto.getAcademicTitle())).thenReturn(Optional.of(academicTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(memberDto.getScientificField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(memberDto.getDepartment())).thenReturn(Optional.of(department));

        Member member = memberConverter.toEntity(memberDto);

        assertEquals(memberDto.getId(), member.getId());
        assertEquals(memberDto.getFirstName(), member.getFirstName());
        assertEquals(memberDto.getAcademicTitle(), member.getAcademicTitle().getTitle());
        assertEquals(memberDto.getEducationTitle(), member.getEducationTitle().getTitle());
        assertEquals(memberDto.getScientificField(), member.getScientificField().getScfField());
        assertEquals(memberDto.getDepartment(), member.getDepartment().getName());

    }

}
