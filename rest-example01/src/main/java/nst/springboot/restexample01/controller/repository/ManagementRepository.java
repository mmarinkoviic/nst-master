package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.Management;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ManagementRepository extends JpaRepository<Management,Long> {

    List<Management> findByDepartmentId (Long id);
    List<Management> findByEndDateIsNullAndRole(String role);
    List<Management> findByRole(String role);
    List<Management> findByEndDateIsNullAndDepartmentId(Long id);
    List<Management> findByDepartmentIdAndRole(Long id, String role);
    Optional<Management> findByEndDateIsNullAndDepartmentIdAndRole(Long id, String role);
    List<Management> findByMemberId(Long id);
    @Query("SELECT MAX(m.id) FROM Management m")
    Long findMaxId();
}
