package nst.springboot.restexample01.controller;

import nst.springboot.restexample01.controller.service.EducationTitleService;
import nst.springboot.restexample01.dto.EducationTitleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education-title")
public class EducationTitleController {

    private EducationTitleService educationTitleService;

    public EducationTitleController(EducationTitleService educationTitleService) {
        this.educationTitleService = educationTitleService;
    }
    @PostMapping("/save")
    public ResponseEntity<EducationTitleDto> save (@RequestParam("Education title") String name) throws Exception{
        EducationTitleDto educationTitle = educationTitleService.save(name);
        return new ResponseEntity<>(educationTitle, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EducationTitleDto>> getAll() {
        List<EducationTitleDto> educationTitleDtoList = educationTitleService.getAll();
        return new ResponseEntity<>(educationTitleDtoList,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete (@RequestParam("ID") Long id) throws Exception{
        educationTitleService.delete(id);
        return new ResponseEntity<>("Education title with id " + id + " removed!",HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EducationTitleDto> findById (@PathVariable("id") Long id) throws Exception{
        EducationTitleDto educationTitleDto = educationTitleService.findById(id);
        return new ResponseEntity<>(educationTitleDto,HttpStatus.OK);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update (@PathVariable("id") Long id, @RequestParam("New name") String newName) throws Exception{
        educationTitleService.update(id,newName);
        return new ResponseEntity<>("Education title updated!",HttpStatus.OK);
    }

}
