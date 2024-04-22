package nst.springboot.restexample01.controller;

import nst.springboot.restexample01.service.ScientificFieldService;
import nst.springboot.restexample01.dto.ScientificFieldDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scientific-field")
public class ScientificFieldController {
    private ScientificFieldService scientificFieldService;

    public ScientificFieldController(ScientificFieldService scientificFieldService) {
        this.scientificFieldService = scientificFieldService;
    }

    @PostMapping()
    public ResponseEntity<ScientificFieldDto> save (@RequestParam("Scientific field") String name) throws Exception{
        ScientificFieldDto scientificFieldDto = scientificFieldService.save(name);
        return new ResponseEntity<>(scientificFieldDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScientificFieldDto>> getAll() {
        List<ScientificFieldDto> scientificFieldDtoList = scientificFieldService.getAll();
        return new ResponseEntity<>(scientificFieldDtoList,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete (@RequestParam("ID") Long id) throws Exception{
        scientificFieldService.delete(id);
        return new ResponseEntity<>("Scientific field with id " + id + " removed!",HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ScientificFieldDto> findById (@PathVariable("id") Long id) throws Exception{
        ScientificFieldDto scientificFieldDto = scientificFieldService.findById(id);
        return new ResponseEntity<>(scientificFieldDto,HttpStatus.OK);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update (@PathVariable("id") Long id, @RequestParam("New name") String newName) throws Exception{
        scientificFieldService.update(id,newName);
        return new ResponseEntity<>("Scientific field updated!",HttpStatus.OK);
    }

}
