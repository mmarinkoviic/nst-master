package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
    Optional<Subject> findByName(String name);

    List<Subject> findByDepartmentName(String department);
    List<Subject> findByDepartmentId(Long id);
}
