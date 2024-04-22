package nst.springboot.restexample01.service;


import nst.springboot.restexample01.dto.ScientificFieldDto;

import java.util.List;

public interface ScientificFieldService {
    ScientificFieldDto save (String name) throws Exception;
    List<ScientificFieldDto> getAll();
    void delete (Long id) throws Exception;
    ScientificFieldDto findById(Long id) throws Exception;
    void update (Long id, String newName) throws Exception;
}
