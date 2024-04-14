package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByDepartmentId(Long id);
    List<Member> findByAcademicTitleId(Long id);
    List<Member> findByEducationTitleId (Long id);
    List<Member> findByScientificFieldId (Long id);
}
