package nst.springboot.restexample01.controller.repository;


import nst.springboot.restexample01.controller.domain.ScientificField;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ScientificFieldRepository extends JpaRepository<ScientificField, Long> {
    Optional<ScientificField> findByScfField(String name);
}
