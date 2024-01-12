package nst.springboot.restexample01.controller;

import nst.springboot.restexample01.controller.service.AcademicTitleHistoryService;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/academic-title-history")
public class AcademicTitleHistoryController {
    private final AcademicTitleHistoryService academicTitleHistoryService;

    public AcademicTitleHistoryController(AcademicTitleHistoryService academicTitleHistoryService) {
        this.academicTitleHistoryService = academicTitleHistoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAll() {
        List<String> academicTitleHistoryList = academicTitleHistoryService.getAll();
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        academicTitleHistoryService.delete(id);
        return new ResponseEntity<>("Record removed!", HttpStatus.OK);
    }

    @GetMapping("/find-by-id")
    public String findById(@RequestParam("ID") Long id) throws Exception {
        return academicTitleHistoryService.findById(id);
    }

    @GetMapping("/by-member")
    public ResponseEntity<List<String>> getAllByMember(@RequestParam("First Name") String firstName,
                                                       @RequestParam("Last Name") String lastName) throws Exception{
        List<String> academicTitleHistoryList = academicTitleHistoryService.getAllByMember(firstName,lastName);
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }

    @GetMapping("/by-scf-field")
    public ResponseEntity<List<String>> getAllByScientificField(@RequestParam("Scientific Field") String scfField) throws Exception{
        List<String> academicTitleHistoryList = academicTitleHistoryService.getAllByScfField(scfField);
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }

    @GetMapping("/members-with-current-academic-titles")
    public ResponseEntity<List<String>> getAllCurrent() throws Exception{
        List<String> academicTitleHistoryList = academicTitleHistoryService.getAllCurrently();
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }


    @GetMapping("/current-by-academic-title")
    public ResponseEntity<List<String>> getAllCurrentByAcademicTitle(@RequestParam("Academic Title") String academicTitle) throws Exception{
        List<String> academicTitleHistoryList = academicTitleHistoryService.getAllCurrentlyByAcademicTitle(academicTitle);
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AcademicTitleHistoryDto> save(@RequestParam("First Name") String firstName,
                                                        @RequestParam("Last Name") String lastName,
                                                        @RequestParam("Academic Title") String academicTitle,
                                                        @RequestParam("Start Date") LocalDate startDate) throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = academicTitleHistoryService.save(firstName,lastName,startDate,academicTitle);
        return new ResponseEntity<>(academicTitleHistoryDto, HttpStatus.CREATED);
    }

}
