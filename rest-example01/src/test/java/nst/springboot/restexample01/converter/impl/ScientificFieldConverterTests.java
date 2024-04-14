package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.ScientificField;
import nst.springboot.restexample01.dto.ScientificFieldDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScientificFieldConverterTests {
    private ScientificFieldConverter scientificFieldConverter;

    @BeforeEach
    public void setUp(){
        scientificFieldConverter = new ScientificFieldConverter();
    }
    @Test
    @DisplayName("Test converting ScientificField entity to ScientificFieldDto")
    public void toDtoTest(){
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        ScientificFieldDto scientificFieldDto = scientificFieldConverter.toDto(scientificField);

        assertEquals(scientificField.getId(), scientificFieldDto.getId());
        assertEquals(scientificField.getScfField(), scientificFieldDto.getScfField());
    }
    @Test
    @DisplayName("Test converting ScientificFieldDto to ScientificField entity")
    public void toEntityTest (){
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(2L,"ScF2");
        ScientificField scientificField = scientificFieldConverter.toEntity(scientificFieldDto);

        assertEquals(scientificFieldDto.getId(), scientificField.getId());
        assertEquals(scientificFieldDto.getScfField(), scientificField.getScfField());
    }
}
