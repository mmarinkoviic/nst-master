package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.EducationTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EducationTitleRepository extends JpaRepository<EducationTitle, Long> {
    Optional<EducationTitle> findByTitleIgnoreCase(String name);
    @Query("SELECT MAX(et.id) FROM EducationTitle et")
    Long findMaxId();
}
