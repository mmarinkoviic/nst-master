package nst.springboot.restexample01.controller.service;

import nst.springboot.restexample01.dto.ManagementDto;

import java.time.LocalDate;
import java.util.List;

public interface ManagementService {

    ManagementDto save (String departmentName, String firstName, String lastName, String role, LocalDate startDate) throws Exception;
    void update (String department, String role, LocalDate date) throws Exception;
    String findById (Long id) throws Exception;
    String findMemberByRoleAndDepartment(String departmentName, String role) throws Exception;
    List<String> getAll ();
    List<String> findByDepartment (String nameDepartment) throws Exception;
    List<String> findCurrentDepartment (String nameDepartment) throws Exception;
    List<String> currentManagement () throws Exception;
    List<String> currentHandlers () throws Exception;
    List<String> currentSecretary () throws Exception;
    void delete (Long id) throws Exception;
    String printing (ManagementDto managementDto);

}
