package nst.springboot.restexample01.controller;

import jakarta.validation.Valid;
import java.util.List;
import nst.springboot.restexample01.controller.service.SubjectService;
import nst.springboot.restexample01.dto.SubjectDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/save")
    public ResponseEntity<SubjectDto> save(@RequestParam("Subject Name") String nameSubject,
                                           @RequestParam("ESPB") int espb,
                                           @RequestParam("Department Name") String nameDepartment) throws Exception {
        SubjectDto subjectDto = subjectService.save(nameSubject,espb,nameDepartment);
        return new ResponseEntity<>(subjectDto, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<String>> getAll() {
        List<String> subjects = subjectService.getAll();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/find-by-id")
    public String findById(@RequestParam("ID") Long id) throws Exception {
        return subjectService.findById(id).toString();
    }

    @GetMapping("/find-by-name")
    public String findByName(@RequestParam("Subject Name") String name) throws Exception {
        return subjectService.findByName(name);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateSubject (@Valid @RequestBody SubjectDto subject) throws Exception{
        subjectService.update(subject);
        return new ResponseEntity<>("Subject updated!",HttpStatus.OK);
    }

    @DeleteMapping("/delete-id")
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        subjectService.delete(id);
        return new ResponseEntity<>("Subject removed!", HttpStatus.OK);
    }

    @DeleteMapping("/delete-name")
    public ResponseEntity<String> delete(@RequestParam("Subject Name") String name) throws Exception {
        subjectService.delete(name);
        return new ResponseEntity<>("Subject removed!", HttpStatus.OK);
    }
    @GetMapping("/by-department")
    public ResponseEntity<List<String>> getByDepartmentName(@RequestParam("Department Name") String departmentName) throws Exception {
        List<String> subjects = subjectService.findByDepartmentName(departmentName);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }
}
