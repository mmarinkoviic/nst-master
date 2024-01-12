package nst.springboot.restexample01.controller;

import jakarta.validation.Valid;
import java.util.List;
import nst.springboot.restexample01.controller.service.DepartmentService;
import nst.springboot.restexample01.controller.service.ManagementService;
import nst.springboot.restexample01.dto.DepartmentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private DepartmentService departmentService;
    private ManagementService managementService;

    public DepartmentController(DepartmentService departmentService, ManagementService managementService) {
        this.departmentService = departmentService;
        this.managementService = managementService;
    }

    @PostMapping("/save")
    public ResponseEntity<DepartmentDto> save(@Valid @RequestParam("Department Name") String departmentDto) throws Exception {
        DepartmentDto deptDto = departmentService.save(departmentDto);
        return new ResponseEntity<>(deptDto, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<String>> getAll() {
        List<String> departments = departmentService.getAll();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }


    @GetMapping("/find-by-id")
    public ResponseEntity<DepartmentDto> findById(@RequestParam("ID") Long id) throws Exception {
        DepartmentDto departmentDto = departmentService.findById(id);
        return new ResponseEntity<>(departmentDto,HttpStatus.OK);
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<DepartmentDto> findByName(@RequestParam("Department Name") String name) throws Exception {
        DepartmentDto departmentDto = departmentService.findByName(name);
        return new ResponseEntity<>(departmentDto,HttpStatus.OK);
    }

    @GetMapping("/management-of-department")
    public ResponseEntity<List<String>> getManagement (@RequestParam("Department Name") String departmentName) throws Exception{
        List<String> management = managementService.findCurrentDepartment(departmentName);
        return new ResponseEntity<>(management,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDepartment (@Valid @RequestBody DepartmentDto departmentDto) throws Exception{
        departmentService.update(departmentDto);
        return new ResponseEntity<>("Department updated!",HttpStatus.OK);
    }

    @DeleteMapping("/delete-id")
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        departmentService.delete(id);
        return new ResponseEntity<>("Department with id " + id + " removed!", HttpStatus.OK);
    }

    @DeleteMapping("/delete-name")
    public ResponseEntity<String> delete (@RequestParam("Department Name") String name) throws Exception{
        departmentService.delete(name);
        return new ResponseEntity<>("Department witch name is "+ name + " removed!",HttpStatus.OK);
    }

}
