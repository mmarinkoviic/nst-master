package nst.springboot.restexample01.controller;

import jakarta.validation.Valid;
import nst.springboot.restexample01.controller.domain.AcademicTitleHistory;
import nst.springboot.restexample01.controller.service.MemberService;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<MemberDto>> getAll() {
        List<MemberDto> members = memberService.getAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/find-by-id")
    public MemberDto findById(@RequestParam("ID") Long id) throws Exception {
        return memberService.findById(id);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        memberService.delete(id);
        return new ResponseEntity<>("Member removed!", HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<MemberDto> save(@RequestParam("First Name") String firstName,
                                          @RequestParam("Last Name") String lastName,
                                          @RequestParam("Academic Title") String academicTitle,
                                          @RequestParam("Education Title") String educationTitle,
                                          @RequestParam("Scientific Filed") String scientificField,
                                          @RequestParam("Department") String department) throws Exception {
        MemberDto memberDto = memberService.save(firstName,lastName,academicTitle,educationTitle,scientificField,department);
        return new ResponseEntity<>(memberDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMember (@Valid @RequestBody MemberDto memberDto) throws Exception{
        memberService.update(memberDto);
        return new ResponseEntity<>("Member updated!",HttpStatus.OK);
    }
    @GetMapping("{id}/academic-title-history")
    public ResponseEntity<List<AcademicTitleHistoryDto>> getHistory(@PathVariable("id") Long id) throws Exception{
        List<AcademicTitleHistoryDto> academicTitleHistoryDtos = memberService.getHistory(id);
        return new ResponseEntity<>(academicTitleHistoryDtos,HttpStatus.OK);
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<MemberDto>> getAllByDepartment(@RequestParam("Department Name") String department) throws Exception{
        List<MemberDto> memberList = memberService.getAllByDepartment(department);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }

    @GetMapping("/by-academic-title")
    public ResponseEntity<List<String>> getAllByAcademicTitle(@RequestParam("Academic Title") String academicTitle) throws Exception{
        List<String> memberList = memberService.getAllByAcademicTitle(academicTitle);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
    @GetMapping("/by-education-title")
    public ResponseEntity<List<String>> getAllByEducationTitle(@RequestParam("Education Title") String educationTitle) throws Exception{
        List<String> memberList = memberService.getAllByEducationTitle(educationTitle);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
    @GetMapping("/by-scientific-field")
    public ResponseEntity<List<String>> getAllByScientificField(@RequestParam("Scientific Field") String scientificField) throws Exception{
        List<String> memberList = memberService.getAllByScientificField(scientificField);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
}
