package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.EducationTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EducationTitleRepository extends JpaRepository<EducationTitle, Long> {
    Optional<EducationTitle> findByTitleIgnoreCase(String name);
    @Query("SELECT MAX(et.id) FROM EducationTitle et")
    Long findMaxId();
}
