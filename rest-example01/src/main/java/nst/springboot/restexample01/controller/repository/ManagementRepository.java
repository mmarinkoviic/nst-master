package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.Management;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ManagementRepository extends JpaRepository<Management,Long> {
    List<Management> findByEndDateIsNull();
    List<Management> findByDepartmentName (String name);
    List<Management> findByDepartmentId (Long id);
    List<Management> findByEndDateIsNullAndRole(String role);
    List<Management> findByEndDateIsNullAndDepartmentName(String name);
    List<Management> findByEndDateIsNullAndDepartmentId(Long id);
    Optional<Management> findByEndDateIsNullAndDepartmentNameAndRole(String name, String role);
    Optional<Management> findByEndDateIsNullAndDepartmentIdAndRole(Long id, String role);
    List<Management> findByMemberId(Long id);
}
