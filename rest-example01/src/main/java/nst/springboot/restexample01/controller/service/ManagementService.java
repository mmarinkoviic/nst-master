package nst.springboot.restexample01.controller.service;

import nst.springboot.restexample01.dto.ManagementDto;

import java.time.LocalDate;
import java.util.List;

public interface ManagementService {

    ManagementDto save (String departmentName, String firstName, String lastName, String role, LocalDate startDate) throws Exception;
    void update (String department, String role, LocalDate date) throws Exception;
    ManagementDto findById (Long id) throws Exception;
    List<ManagementDto> getAll ();
    List<ManagementDto> findByDepartment (Long id) throws Exception;
    List<ManagementDto> findCurrentDepartment (Long id) throws Exception;
    List<ManagementDto> currentHandlers () throws Exception;
    List<ManagementDto> currentSecretary () throws Exception;
    void delete (Long id) throws Exception;


}
