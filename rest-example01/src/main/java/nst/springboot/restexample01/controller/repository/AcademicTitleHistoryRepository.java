package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.AcademicTitleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademicTitleHistoryRepository extends JpaRepository<AcademicTitleHistory, Long> {

    List<AcademicTitleHistory> findByMemberId(Long memberId);
    Optional<AcademicTitleHistory> findByEndDateIsNullAndMemberId (Long memberId);
    Optional<AcademicTitleHistory> findByMemberIdAndAcademicTitleTitle(Long id, String academicTitle);

}
