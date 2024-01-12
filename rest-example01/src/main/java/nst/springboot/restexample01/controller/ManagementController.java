package nst.springboot.restexample01.controller;

import nst.springboot.restexample01.controller.service.ManagementService;
import nst.springboot.restexample01.dto.ManagementDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/management")
public class ManagementController {
    private final ManagementService managementService;

    public ManagementController(ManagementService managementService){this.managementService = managementService;}

    @PostMapping("/save")
    public ResponseEntity<ManagementDto> save(@RequestParam("Department Name") String departmentName,
                                              @RequestParam("First Name") String firstName,
                                              @RequestParam("Last Name") String lastName,
                                              @RequestParam("Role") String role,
                                              @RequestParam("Start Date") LocalDate startDate) throws Exception {

        ManagementDto managementDto = managementService.save(departmentName,firstName,lastName,role,startDate);
        return new ResponseEntity<>(managementDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id")
    public String findById (@RequestParam("ID") Long id) throws Exception{
        return managementService.findById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<String>> getAll() {
        List<String> managements = managementService.getAll();
        return new ResponseEntity<>(managements, HttpStatus.OK);
    }
    @GetMapping("/find-member-by-department-role")
    public ResponseEntity<String> findMemberByDepartmentRole(@RequestParam("Department Name") String departmentName,
                                                             @RequestParam("Role") String role) throws Exception{
        String management = managementService.findMemberByRoleAndDepartment(departmentName,role);
        return new ResponseEntity<>(management,HttpStatus.OK);
    }

    @GetMapping("/history-of-department")
    public ResponseEntity<List<String>> historyOfDepartment(@RequestParam("Department Name") String departmentName) throws Exception{
        List<String> management = managementService.findByDepartment(departmentName);
        return new ResponseEntity<>(management,HttpStatus.OK);
    }

    @GetMapping("/management-of-department")
    public ResponseEntity<List<String>> managementOfDepartment(@RequestParam("Department Name") String departmentName) throws Exception{
        List<String> management = managementService.findCurrentDepartment(departmentName);
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @GetMapping("/current-management-of-all-departments")
    public ResponseEntity<List<String>> managementOfAllDepartments() throws Exception{
        List<String> management = managementService.currentManagement();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @GetMapping("/current-handlers")
    public ResponseEntity<List<String>> currentHandlers() throws Exception{
        List<String> management = managementService.currentHandlers();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @GetMapping("/current-secretaries")
    public ResponseEntity<List<String>> currentSecretaries() throws Exception{
        List<String> management = managementService.currentSecretary();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        managementService.delete(id);
        return new ResponseEntity<>("Record removed!", HttpStatus.OK);
    }


}
