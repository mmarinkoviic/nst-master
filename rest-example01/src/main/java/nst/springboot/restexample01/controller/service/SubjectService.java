package nst.springboot.restexample01.controller.service;

import java.util.List;
import nst.springboot.restexample01.dto.SubjectDto;

public interface SubjectService {
    SubjectDto save(String nameSubject, int espb, String nameDepartment)throws Exception;
    List<String> getAll();
    void delete(Long id) throws Exception;
    void delete (String nameSubject) throws Exception;
    void update(SubjectDto subjectDto)throws Exception;
    String findById(Long id)throws Exception;
    String findByName(String nameSubject) throws Exception;
    List<String> findByDepartmentName(String department) throws Exception;

}
