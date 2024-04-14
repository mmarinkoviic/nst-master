package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
import nst.springboot.restexample01.repository.AcademicTitleRepository;
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
import static org.mockito.Mockito.when;

@SpringBootTest
public class AcademicTitleHistoryConverterTests {
    private AcademicTitleHistoryConverter academicTitleHistoryConverter;
    @MockBean
    @Autowired
    private MemberConverter memberConverter;
    @Autowired
    @MockBean
    private AcademicTitleRepository academicTitleRepository;
    @Autowired
    @MockBean
    private ScientificFieldRepository scientificFieldRepository;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        academicTitleHistoryConverter = new AcademicTitleHistoryConverter();

        Field fieldMC =AcademicTitleHistoryConverter.class.getDeclaredField("memberConverter");
        fieldMC.setAccessible(true);
        fieldMC.set(academicTitleHistoryConverter, memberConverter);

        Field fieldAT =AcademicTitleHistoryConverter.class.getDeclaredField("academicTitleRepository");
        fieldAT.setAccessible(true);
        fieldAT.set(academicTitleHistoryConverter, academicTitleRepository);

        Field fieldSF =AcademicTitleHistoryConverter.class.getDeclaredField("scientificFieldRepository");
        fieldSF.setAccessible(true);
        fieldSF.set(academicTitleHistoryConverter, scientificFieldRepository);
    }

    @Test
    @DisplayName("Test converting AcademicTitleHistory entity to AcademicTitleHistoryDto")
    public void toDtoTest(){
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Department department = new Department(1L,"Department1");

        Member member = new Member(1L,"Lucy","Parker",academicTitle, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(1L,"Lucy","Parker",academicTitle.getTitle(),
                educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory(1L, member,
                LocalDate.of(2023,4,4),LocalDate.of(2024,5,5),academicTitle,scientificField);

        when(memberConverter.toDto(member)).thenReturn(memberDto);

        AcademicTitleHistoryDto academicTitleHistoryDto = academicTitleHistoryConverter.toDto(academicTitleHistory);

        assertEquals(academicTitleHistory.getId(), academicTitleHistoryDto.getId());
        assertEquals(academicTitleHistory.getStartDate(), academicTitleHistoryDto.getStartDate());
        assertEquals(academicTitleHistory.getEndDate(), academicTitleHistoryDto.getEndDate());
    }

    @Test
    @DisplayName("Test converting AcademicTitleHistoryDto to AcademicTitleHistory entity")
    public void toEntityTest (){
        Department department = new Department(2L,"Department2");
        AcademicTitle academicTitle = new AcademicTitle(2L,"AT2");
        EducationTitle educationTitle = new EducationTitle(2L,"ET2");
        ScientificField scientificField = new ScientificField(2L,"ScF2");
        MemberDto memberDto = new MemberDto(2L,"Lucy", "Parker", academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member = new Member(2L,"Lucy","Parker",academicTitle, educationTitle, scientificField, department);

        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(2L, memberDto, LocalDate.of(2023,4,4),
                LocalDate.of(2024,5,5), academicTitle.getTitle(), scientificField.getScfField());

        when(academicTitleRepository.findByTitleIgnoreCase(academicTitleHistoryDto.getAcademicTitle())).thenReturn(Optional.of(academicTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(academicTitleHistoryDto.getScientificField())).thenReturn(Optional.of(scientificField));
        when(memberConverter.toEntity(academicTitleHistoryDto.getMemberDto())).thenReturn(member);

        AcademicTitleHistory academicTitleHistory = academicTitleHistoryConverter.toEntity(academicTitleHistoryDto);

        assertEquals(academicTitleHistoryDto.getId(), academicTitleHistory.getId());
        assertEquals(academicTitleHistoryDto.getStartDate(), academicTitleHistory.getStartDate());
        assertEquals(academicTitleHistoryDto.getEndDate(), academicTitleHistory.getEndDate());

    }

}
