package nst.springboot.restexample01.controller.service;

import nst.springboot.restexample01.dto.MemberDto;
import java.util.List;

public interface MemberService {
    MemberDto save(String firstName, String lastName, String academicTitle, String educationTitle, String
            scientificField, String department) throws Exception;
    List<String> getAll();
    void delete (Long id) throws Exception;
    void delete (String firstName, String lastName) throws Exception;
    void update (MemberDto memberDto) throws Exception;
    MemberDto findById (Long id) throws Exception;
    MemberDto findByFirstLastName (String firstName, String lastName) throws Exception;
    List<String> getAllByDepartment(String department) throws Exception;
    List<String> getAllByAcademicTitle(String academicTitle) throws Exception;
    List<String> getAllByEducationTitle(String educationTitle) throws Exception;
    List<String> getAllByScientificField(String scientificField) throws Exception;
}
