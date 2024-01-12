package nst.springboot.restexample01.controller.service;

import java.util.List;
import nst.springboot.restexample01.dto.DepartmentDto;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    DepartmentDto save(String name) throws Exception;
    List<String> getAll();
    void delete(Long id) throws Exception;
    void delete (String name) throws  Exception;
    void update(DepartmentDto department) throws Exception;
    DepartmentDto findById(Long id) throws Exception;
    DepartmentDto findByName(String name) throws Exception;
}
