package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.EducationTitle;
import nst.springboot.restexample01.domain.ScientificField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ScientificFieldRepositoryTests {
    @Autowired
    private ScientificFieldRepository scientificFieldRepository;

    @Test
    public void findByTitleIgnoreCaseTest(){
        ScientificField scientificField = scientificFieldRepository.save(new ScientificField(1L,"scientificField1"));
        assertNotNull(scientificField);
        Optional<ScientificField> scF = scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField());
        assertTrue(scF.isPresent());
        assertEquals(scientificField,scF.get());
    }
    @Test
    public void findMaxIdTest(){
        ScientificField scientificField1 = scientificFieldRepository.save(new ScientificField(1L,"MAX1"));
        ScientificField scientificField2 = scientificFieldRepository.save(new ScientificField(2L,"MAX2"));
        ScientificField scientificField3 = scientificFieldRepository.save(new ScientificField(3L,"MAX3"));
        assertNotNull(scientificField1);
        assertNotNull(scientificField2);
        assertNotNull(scientificField3);
        Long maxID = scientificFieldRepository.findMaxId();
        assertEquals(scientificField3.getId(),maxID);
    }
}
