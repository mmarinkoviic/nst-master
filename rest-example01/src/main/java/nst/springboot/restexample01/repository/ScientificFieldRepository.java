package nst.springboot.restexample01.repository;


import nst.springboot.restexample01.domain.ScientificField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScientificFieldRepository extends JpaRepository<ScientificField, Long> {
    Optional<ScientificField> findByScfFieldIgnoreCase(String name);
    @Query("SELECT MAX(sf.id) FROM ScientificField sf")
    Long findMaxId();
    void deleteByScfFieldIgnoreCase (String name);
}
