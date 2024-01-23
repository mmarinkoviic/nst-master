package nst.springboot.restexample01.controller.service;

import java.util.List;
import java.util.logging.Handler;

import nst.springboot.restexample01.controller.domain.Subject;
import nst.springboot.restexample01.dto.DepartmentDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;

public interface DepartmentService {
    DepartmentDto save(String name) throws Exception;
    List<DepartmentDto> getAll();
    void delete(Long id) throws Exception;
    void update(Long id, String newName) throws Exception;
    DepartmentDto findById(Long id) throws Exception;
    void putSecretary(Long departmentId,Long memberId) throws Exception;
    void putHandler (Long departmentId, Long memberId) throws Exception;
    MemberDto getSecretary(Long id) throws Exception;
    MemberDto getHandler(Long id) throws Exception;
    List<SubjectDto> getSubjects (Long id) throws Exception;
    List<MemberDto> getMembers (Long id) throws Exception;

}
