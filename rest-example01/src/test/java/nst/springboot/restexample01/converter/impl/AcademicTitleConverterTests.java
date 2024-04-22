package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.AcademicTitle;
import nst.springboot.restexample01.dto.AcademicTitleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class AcademicTitleConverterTests {
    private AcademicTitleConverter academicTitleConverter;

    @BeforeEach
    public void setUp(){
        academicTitleConverter = new AcademicTitleConverter();
    }
    @Test
    @DisplayName("Test converting AcademicTitle entity to AcademicTitleDto")
    public void toDtoTest(){
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        AcademicTitleDto academicTitleDto = academicTitleConverter.toDto(academicTitle);

        assertEquals(academicTitle.getId(),academicTitleDto.getId());
        assertEquals(academicTitle.getTitle(),academicTitleDto.getTitle());
    }
    @Test
    @DisplayName("Test converting AcademicTitleDto to AcademicTitle entity")
    public void toEntityTest (){
        AcademicTitleDto academicTitleDto = new AcademicTitleDto(2L,"ET2");
        AcademicTitle academicTitle = academicTitleConverter.toEntity(academicTitleDto);

        assertEquals(academicTitleDto.getId(),academicTitle.getId());
        assertEquals(academicTitleDto.getTitle(),academicTitle.getTitle());
    }
}
