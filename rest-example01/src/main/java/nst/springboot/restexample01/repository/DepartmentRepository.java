package nst.springboot.restexample01.repository;

import java.util.Optional;
import nst.springboot.restexample01.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DepartmentRepository extends JpaRepository<Department, Long>{

    Optional<Department> findByNameIgnoreCase(String name);
    @Query("SELECT MAX(d.id) FROM Department d")
    Long findMaxId();
    void deleteByNameIgnoreCase(String name);
}
