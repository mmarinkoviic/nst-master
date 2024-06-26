package nst.springboot.restexample01.controller;

import jakarta.validation.Valid;
import java.util.List;

import nst.springboot.restexample01.service.DepartmentService;
import nst.springboot.restexample01.dto.DepartmentDto;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping()
    public ResponseEntity<DepartmentDto> save(@Valid @RequestParam("Department Name") String departmentDto) throws Exception {
        DepartmentDto deptDto = departmentService.save(departmentDto);
        return new ResponseEntity<>(deptDto, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<DepartmentDto>> getAll() {
        List<DepartmentDto> departments = departmentService.getAll();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable("id") Long id) throws Exception {
        DepartmentDto departmentDto = departmentService.findById(id);
        return new ResponseEntity<>(departmentDto,HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateDepartment (@PathVariable("id") Long id,@RequestParam("Department") String newName) throws Exception{
        departmentService.update(id, newName);
        return new ResponseEntity<>("Department updated!",HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        departmentService.delete(id);
        return new ResponseEntity<>("Department with id " + id + " removed!", HttpStatus.OK);
    }

    @GetMapping("/{id}/secretary")
    public ResponseEntity<MemberDto> findSecretary (@PathVariable("id") Long id) throws Exception{
        MemberDto memberDto = departmentService.getSecretary(id);
        return new ResponseEntity<>(memberDto,HttpStatus.OK);
    }
    @PatchMapping("/{id}/updateSecretary")
    public ResponseEntity<String> putSecretary(@PathVariable("id") Long departmentId, @RequestParam Long memberId) throws Exception{
        departmentService.putSecretary(departmentId,memberId);
        return new ResponseEntity<>("Secretary posted!",HttpStatus.OK);
    }
    @PatchMapping("/{id}/updateHandler")
    public ResponseEntity<String> putHandler(@PathVariable("id") Long departmentId, @RequestParam Long memberId) throws Exception{
        departmentService.putHandler(departmentId,memberId);
        return new ResponseEntity<>("Handler posted!",HttpStatus.OK);
    }
    @GetMapping("/{id}/handler")
    public ResponseEntity<MemberDto> findHandler (@PathVariable("id") Long id) throws Exception{
        MemberDto memberDto = departmentService.getHandler(id);
        return new ResponseEntity<>(memberDto,HttpStatus.OK);
    }
    @GetMapping("/{id}/handler/history")
    public ResponseEntity<List<ManagementDto>> findHandlerHistory (@PathVariable("id") Long id) throws Exception{
        List<ManagementDto> managementDtos = departmentService.getHandlerHistory(id);
        return new ResponseEntity<>(managementDtos,HttpStatus.OK);
    }
    @GetMapping("/{id}/secretary/history")
    public ResponseEntity<List<ManagementDto>> findSecretaryHistory (@PathVariable("id") Long id) throws Exception{
        List<ManagementDto> managementDtos = departmentService.getSecretaryHistory(id);
        return new ResponseEntity<>(managementDtos,HttpStatus.OK);
    }
    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<SubjectDto>> findSubjects(@PathVariable("id") Long id) throws Exception{
        List<SubjectDto> subjectDtoList = departmentService.getSubjects(id);
        return new ResponseEntity<>(subjectDtoList,HttpStatus.OK);
    }
    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberDto>> findMembers(@PathVariable("id") Long id) throws Exception{
        List<MemberDto> memberDtoList = departmentService.getMembers(id);
        return new ResponseEntity<>(memberDtoList,HttpStatus.OK);
    }
}
