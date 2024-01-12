package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.EducationTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EducationTitleRepository extends JpaRepository<EducationTitle, Long> {
    Optional<EducationTitle> findByTitle(String name);
}
