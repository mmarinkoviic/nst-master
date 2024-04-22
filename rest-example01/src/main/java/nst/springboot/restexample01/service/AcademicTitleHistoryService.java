package nst.springboot.restexample01.service;

import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import java.time.LocalDate;
import java.util.List;

public interface AcademicTitleHistoryService {

    AcademicTitleHistoryDto save (Long memberId, LocalDate startDate, String academicTitle) throws Exception;
    AcademicTitleHistoryDto savePrevious (Long memberId, LocalDate startDate, LocalDate endDate, String academicTitle) throws Exception;
    List<AcademicTitleHistoryDto> getAll();
    void delete (Long id) throws Exception;
    void update (Long id, LocalDate endDate) throws Exception;
    AcademicTitleHistoryDto findById (Long id) throws Exception;
    List <AcademicTitleHistoryDto> getAllByMember (Long id) throws Exception;

}
