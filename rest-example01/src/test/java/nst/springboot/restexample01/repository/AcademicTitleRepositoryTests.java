package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.AcademicTitle;
import nst.springboot.restexample01.domain.EducationTitle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AcademicTitleRepositoryTests {
    @Autowired
    private AcademicTitleRepository academicTitleRepository;

    @Test
    @Transactional
    public void findByTitleIgnoreCaseTest(){
        academicTitleRepository.deleteByTitleIgnoreCase("academicTitle1000");
        AcademicTitle academicTitle = academicTitleRepository.save(new AcademicTitle(1000L,"academicTitle1000"));
        assertNotNull(academicTitle);
        Optional<AcademicTitle> ac = academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle());
        assertTrue(ac.isPresent());
        assertEquals(academicTitle,ac.get());
    }
    @Test
    public void findMaxIdTest(){
        AcademicTitle academicTitle1 = academicTitleRepository.save(new AcademicTitle(1L,"MAX1"));
        AcademicTitle academicTitle2 = academicTitleRepository.save(new AcademicTitle(2L,"MAX2"));
        AcademicTitle academicTitle3 = academicTitleRepository.save(new AcademicTitle(3L,"MAX3"));
        assertNotNull(academicTitle1);
        assertNotNull(academicTitle2);
        assertNotNull(academicTitle3);
        Long maxID = academicTitleRepository.findMaxId();
        assertEquals(academicTitle3.getId(),maxID);
    }
}
