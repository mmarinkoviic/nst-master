package nst.springboot.restexample01.service;

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
    List<MemberDto> getAllByDepartment(Long id) throws Exception;
    List<MemberDto> getAllByAcademicTitle(Long id) throws Exception;
    List<MemberDto> getAllByEducationTitle(Long id) throws Exception;
    List<MemberDto> getAllByScientificField(Long id) throws Exception;
}
