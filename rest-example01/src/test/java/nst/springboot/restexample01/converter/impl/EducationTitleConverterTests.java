package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.converter.impl.EducationTitleConverter;
import nst.springboot.restexample01.domain.EducationTitle;
import nst.springboot.restexample01.dto.EducationTitleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class EducationTitleConverterTests {
    private EducationTitleConverter educationTitleConverter;

    @BeforeEach
    public void setUp(){
        educationTitleConverter = new EducationTitleConverter();
    }
    @Test
    @DisplayName("Test converting EducationTitle entity to EducationTitleDto")
    public void toDtoTest(){
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        EducationTitleDto educationTitleDto = educationTitleConverter.toDto(educationTitle);

        assertEquals(educationTitle.getId(),educationTitleDto.getId());
        assertEquals(educationTitle.getTitle(),educationTitleDto.getTitle());
    }
    @Test
    @DisplayName("Test converting EducationTitleDto to EducationTitle entity")
    public void toEntityTest (){
        EducationTitleDto educationTitleDto = new EducationTitleDto(2L,"ET2");
        EducationTitle educationTitle = educationTitleConverter.toEntity(educationTitleDto);

        assertEquals(educationTitleDto.getId(),educationTitle.getId());
        assertEquals(educationTitleDto.getTitle(),educationTitle.getTitle());
    }
}
