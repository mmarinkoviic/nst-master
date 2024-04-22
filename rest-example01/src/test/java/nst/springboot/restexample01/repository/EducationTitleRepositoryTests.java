package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.EducationTitle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EducationTitleRepositoryTests {
    @Autowired
    private EducationTitleRepository educationTitleRepository;


    @Test
    @Transactional
    public void findByTitleIgnoreCaseTest(){
        educationTitleRepository.deleteByTitleIgnoreCase("educationTitle1000");
        EducationTitle educationTitle = educationTitleRepository.save(new EducationTitle(10L,"educationTitle1000"));
        assertNotNull(educationTitle);
        Optional<EducationTitle> ed = educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle());
        assertTrue(ed.isPresent());
        assertEquals(educationTitle,ed.get());
    }
    @Test
    public void findMaxIdTest(){
        EducationTitle educationTitle1 = educationTitleRepository.save(new EducationTitle(1L,"MAX1"));
        EducationTitle educationTitle2 = educationTitleRepository.save(new EducationTitle(2L,"MAX2"));
        EducationTitle educationTitle3 = educationTitleRepository.save(new EducationTitle(3L,"MAX3"));
        assertNotNull(educationTitle1);
        assertNotNull(educationTitle2);
        assertNotNull(educationTitle3);
        Long maxID = educationTitleRepository.findMaxId();
        assertEquals(educationTitle3.getId(),maxID);
    }
}
