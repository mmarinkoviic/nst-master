package nst.springboot.restexample01.controller.repository;

import nst.springboot.restexample01.controller.domain.AcademicTitleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AcademicTitleHistoryRepository extends JpaRepository<AcademicTitleHistory, Long> {
    List<AcademicTitleHistory> findByMemberFirstNameAndMemberLastName(String firstName, String lastName);
    List<AcademicTitleHistory> findByScientificFieldScfField(String scientificField);
    List<AcademicTitleHistory> findByEndDateIsNull();
    List<AcademicTitleHistory> findByEndDateIsNullAndAcademicTitleTitle(String academicTitle);
    Optional<AcademicTitleHistory> findByEndDateIsNullAndMemberId (Long memberId);
    Optional<AcademicTitleHistory> findByMemberFirstNameAndMemberLastNameAndAcademicTitleTitleAndScientificFieldScfField
            (String firstName, String lastName, String academicTitle, String scientificField);
}
