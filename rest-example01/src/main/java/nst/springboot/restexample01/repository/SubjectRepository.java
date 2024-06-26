package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
    Optional<Subject> findByNameIgnoreCase(String name);
    List<Subject> findByDepartmentId(Long id);
    @Query("SELECT MAX(s.id) FROM Subject s")
    Long findMaxId();
    void deleteByNameIgnoreCase(String name);
}
