package nst.springboot.restexample01.service;

import java.util.List;

import nst.springboot.restexample01.dto.SubjectDto;

public interface SubjectService {
    SubjectDto save(String nameSubject, int espb, String nameDepartment)throws Exception;
    List<SubjectDto> getAll();
    void delete(Long id) throws Exception;
    void update(SubjectDto subjectDto)throws Exception;
    SubjectDto findById(Long id)throws Exception;
    List<SubjectDto> findByDepartment(Long id) throws Exception;

}
