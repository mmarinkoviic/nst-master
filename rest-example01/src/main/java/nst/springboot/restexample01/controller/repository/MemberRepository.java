package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByDepartmentId(Long id);
    List<Member> findByAcademicTitleId(Long id);
    List<Member> findByEducationTitleId (Long id);
    List<Member> findByScientificFieldId (Long id);
}
