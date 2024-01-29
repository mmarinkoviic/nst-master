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
    public ResponseEntity<ManagementDto> save(@RequestParam("Department ID") Long departmentId,
                                              @RequestParam("Member ID") Long memberId,
                                              @RequestParam("Role") String role,
                                              @RequestParam("Start Date") LocalDate startDate) throws Exception {

        ManagementDto managementDto = managementService.save(departmentId,memberId,role,startDate);
        return new ResponseEntity<>(managementDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ManagementDto findById (@PathVariable("id") Long id) throws Exception{
        return managementService.findById(id);
    }

    @GetMapping()
    public ResponseEntity<List<ManagementDto>> getAll() {
        List<ManagementDto> managements = managementService.getAll();
        return new ResponseEntity<>(managements, HttpStatus.OK);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<List<ManagementDto>> historyOfDepartment(@PathVariable("id") Long id) throws Exception{
        List<ManagementDto> management = managementService.findByDepartment(id);
        return new ResponseEntity<>(management,HttpStatus.OK);
    }

    @GetMapping("/department/{id}/current")
    public ResponseEntity<List<ManagementDto>> managementOfDepartment(@PathVariable("id") Long id) throws Exception{
        List<ManagementDto> management = managementService.findCurrentDepartment(id);
        return new ResponseEntity<>(management,HttpStatus.OK);
    }

    @GetMapping("/handlers/current")
    public ResponseEntity<List<ManagementDto>> currentHandlers() throws Exception{
        List<ManagementDto> management = managementService.currentHandlers();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @GetMapping("/handlers")
    public ResponseEntity<List<ManagementDto>> getHandlers() throws Exception{
        List<ManagementDto> management = managementService.getHandlers();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @GetMapping("/secretaries/current")
    public ResponseEntity<List<ManagementDto>> currentSecretaries() throws Exception{
        List<ManagementDto> management = managementService.currentSecretary();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @GetMapping("/secretaries")
    public ResponseEntity<List<ManagementDto>> getSecretaries() throws Exception{
        List<ManagementDto> management = managementService.getSecretaries();
        return new ResponseEntity<>(management,HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        managementService.delete(id);
        return new ResponseEntity<>("Record removed!", HttpStatus.OK);
    }


}
