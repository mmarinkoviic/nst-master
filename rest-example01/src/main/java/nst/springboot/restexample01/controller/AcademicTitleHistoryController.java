package nst.springboot.restexample01.controller;

import nst.springboot.restexample01.service.AcademicTitleHistoryService;
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

    @GetMapping()
    public ResponseEntity<List<AcademicTitleHistoryDto>> getAll() {
        List<AcademicTitleHistoryDto> academicTitleHistoryList = academicTitleHistoryService.getAll();
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        academicTitleHistoryService.delete(id);
        return new ResponseEntity<>("Record removed!", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public AcademicTitleHistoryDto findById(@PathVariable("id") Long id) throws Exception {
        return academicTitleHistoryService.findById(id);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<List<AcademicTitleHistoryDto>> getAllByMember(@PathVariable("id") Long id) throws Exception{
        List<AcademicTitleHistoryDto> academicTitleHistoryList = academicTitleHistoryService.getAllByMember(id);
        return new ResponseEntity<>(academicTitleHistoryList, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AcademicTitleHistoryDto> save(@RequestParam("Id Member") Long id,
                                                        @RequestParam("Academic Title") String academicTitle,
                                                        @RequestParam("Start Date") LocalDate startDate) throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = academicTitleHistoryService.save(id,startDate,academicTitle);
        return new ResponseEntity<>(academicTitleHistoryDto, HttpStatus.CREATED);
    }

    @PostMapping("/save-previous")
    public ResponseEntity<AcademicTitleHistoryDto> savePrevious(@RequestParam("Id Member") Long id,
                                                                @RequestParam("Academic Title") String academicTitle,
                                                                @RequestParam("Start Date") LocalDate startDate,
                                                                @RequestParam("End Date") LocalDate endDate) throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = academicTitleHistoryService.savePrevious(id,startDate,endDate,academicTitle);
        return new ResponseEntity<>(academicTitleHistoryDto, HttpStatus.CREATED);
    }

}
