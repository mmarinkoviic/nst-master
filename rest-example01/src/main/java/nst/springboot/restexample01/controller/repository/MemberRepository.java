package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByDepartmentName(String departmentTitle);
    List<Member> findByDepartmentId(Long id);
    List<Member> findByAcademicTitleTitle(String academicTitleTitle);
    List<Member> findByAcademicTitleId(Long id);
    List<Member> findByEducationTitleTitle (String educationTitleTitle);
    List<Member> findByEducationTitleId (Long id);
    List<Member> findByScientificFieldScfField (String scientificFieldScfField);
    List<Member> findByScientificFieldId (Long id);
    Optional<Member> findByFirstNameAndLastName (String firstName, String lastName);
}
