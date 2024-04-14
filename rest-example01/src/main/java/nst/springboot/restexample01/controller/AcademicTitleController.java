package nst.springboot.restexample01.controller;


import nst.springboot.restexample01.service.AcademicTitleService;
import nst.springboot.restexample01.dto.AcademicTitleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-title")
public class AcademicTitleController {
    private AcademicTitleService academicTitleService;

    public AcademicTitleController(AcademicTitleService academicTitleService) {
        this.academicTitleService = academicTitleService;
    }
    @PostMapping("/save")
    public ResponseEntity<AcademicTitleDto> save (@RequestParam("Academic title") String name) throws Exception{
        AcademicTitleDto academicTitle = academicTitleService.save(name);
        return new ResponseEntity<>(academicTitle, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<AcademicTitleDto>> getAll() {
        List<AcademicTitleDto> academicTitleDtoList = academicTitleService.getAll();
        return new ResponseEntity<>(academicTitleDtoList,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete (@RequestParam("ID") Long id) throws Exception{
        academicTitleService.delete(id);
        return new ResponseEntity<>("Academic title with id " + id + " removed!",HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AcademicTitleDto> findById (@PathVariable("id") Long id) throws Exception{
        AcademicTitleDto academicTitleDto = academicTitleService.findById(id);
        return new ResponseEntity<>(academicTitleDto,HttpStatus.OK);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update (@PathVariable("id") Long id, @RequestParam("New name") String newName) throws Exception{
        academicTitleService.update(id,newName);
        return new ResponseEntity<>("Academic title updated!",HttpStatus.OK);
    }

}
