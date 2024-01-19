package nst.springboot.restexample01.controller.service;

import nst.springboot.restexample01.controller.domain.AcademicTitleHistory;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import java.util.List;

public interface MemberService {
    MemberDto save(String firstName, String lastName, String academicTitle, String educationTitle, String
            scientificField, String department) throws Exception;
    List<MemberDto> getAll();
    void delete (Long id) throws Exception;
    void update (MemberDto memberDto) throws Exception;
    MemberDto findById (Long id) throws Exception;
    List<AcademicTitleHistoryDto> getHistory(Long id) throws Exception;
    List<MemberDto> getAllByDepartment(String name) throws Exception;
    List<String> getAllByAcademicTitle(String academicTitle) throws Exception;
    List<String> getAllByEducationTitle(String educationTitle) throws Exception;
    List<String> getAllByScientificField(String scientificField) throws Exception;
}
