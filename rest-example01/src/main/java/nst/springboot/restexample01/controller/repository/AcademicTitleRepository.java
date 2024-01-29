package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.AcademicTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AcademicTitleRepository extends JpaRepository<AcademicTitle, Long> {
    Optional<AcademicTitle> findByTitleIgnoreCase(String name);
    @Query("SELECT MAX(at.id) FROM AcademicTitle at")
    Long findMaxId();
}
