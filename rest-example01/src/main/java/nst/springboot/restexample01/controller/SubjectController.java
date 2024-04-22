package nst.springboot.restexample01.controller;

import jakarta.validation.Valid;
import java.util.List;

import nst.springboot.restexample01.service.SubjectService;
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

    @PostMapping()
    public ResponseEntity<SubjectDto> save(@RequestParam("Subject Name") String nameSubject,
                                           @RequestParam("ESPB") int espb,
                                           @RequestParam("Department Name") String nameDepartment) throws Exception {
        SubjectDto subjectDto = subjectService.save(nameSubject,espb,nameDepartment);
        return new ResponseEntity<>(subjectDto, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<SubjectDto>> getAll() {
        List<SubjectDto> subjects = subjectService.getAll();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SubjectDto findById(@PathVariable("id") Long id) throws Exception {
        return subjectService.findById(id);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateSubject (@Valid @RequestBody SubjectDto subject) throws Exception{
        subjectService.update(subject);
        return new ResponseEntity<>("Subject updated!",HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        subjectService.delete(id);
        return new ResponseEntity<>("Subject removed!", HttpStatus.OK);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<List<SubjectDto>> getByDepartmentId(@PathVariable("id") Long id) throws Exception {
        List<SubjectDto> subjects = subjectService.findByDepartment(id);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }
}
