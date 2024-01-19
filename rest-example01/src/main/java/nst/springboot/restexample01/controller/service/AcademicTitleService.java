package nst.springboot.restexample01.controller.service;

import nst.springboot.restexample01.controller.domain.AcademicTitle;
import nst.springboot.restexample01.dto.AcademicTitleDto;

import java.util.List;

public interface AcademicTitleService {
    AcademicTitleDto save (String name) throws Exception;
    List<AcademicTitleDto> getAll();
    void delete (Long id) throws Exception;
    AcademicTitleDto findById(Long id) throws Exception;
    void update (Long id, String newName) throws Exception;
}
