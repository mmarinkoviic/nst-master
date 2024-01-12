package nst.springboot.restexample01.controller.service;

import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import java.time.LocalDate;
import java.util.List;

public interface AcademicTitleHistoryService {

    AcademicTitleHistoryDto save (String firstName, String lastName, LocalDate startDate, String academicTitle) throws Exception;
    List<String> getAll();
    void delete (Long id) throws Exception;
    void update (String firstName, String lastName, LocalDate endDate) throws Exception;
    String findById (Long id) throws Exception;
    List <String> getAllByMember (String firstName, String lastName) throws Exception;
    List<String> getAllCurrently ();
    List<String> getAllByScfField (String scfId) throws Exception;
    List<String> getAllCurrentlyByAcademicTitle (String academicTitle) throws Exception;
    String printing (AcademicTitleHistoryDto academicTitleHistoryDto);

}
