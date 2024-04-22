package nst.springboot.restexample01.service;

import nst.springboot.restexample01.dto.EducationTitleDto;
import java.util.List;

public interface EducationTitleService {

    EducationTitleDto save (String name) throws Exception;
    List<EducationTitleDto> getAll();
    void delete (Long id) throws Exception;
    EducationTitleDto findById(Long id) throws Exception;
    void update (Long id, String newName) throws Exception;
}
