package nst.springboot.restexample01.controller;

import jakarta.validation.Valid;
import nst.springboot.restexample01.service.MemberService;
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

    @GetMapping()
    public ResponseEntity<List<MemberDto>> getAll() {
        List<MemberDto> members = memberService.getAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public MemberDto findById(@PathVariable("id") Long id) throws Exception {
        return memberService.findById(id);
    }


    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam("ID") Long id) throws Exception {
        memberService.delete(id);
        return new ResponseEntity<>("Member removed!", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<MemberDto> save(@RequestParam("First Name") String firstName,
                                          @RequestParam("Last Name") String lastName,
                                          @RequestParam("Academic Title") String academicTitle,
                                          @RequestParam("Education Title") String educationTitle,
                                          @RequestParam("Scientific Field") String scientificField,
                                          @RequestParam("Department") String department) throws Exception {
        MemberDto memberDto = memberService.save(firstName,lastName,academicTitle,educationTitle,scientificField,department);
        return new ResponseEntity<>(memberDto, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateMember (@Valid @RequestBody MemberDto memberDto) throws Exception{
        memberService.update(memberDto);
        return new ResponseEntity<>("Member updated!",HttpStatus.OK);
    }
    @GetMapping("{id}/academic-title-history")
    public ResponseEntity<List<AcademicTitleHistoryDto>> getHistory(@PathVariable("id") Long id) throws Exception{
        List<AcademicTitleHistoryDto> academicTitleHistoryDtos = memberService.getHistory(id);
        return new ResponseEntity<>(academicTitleHistoryDtos,HttpStatus.OK);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<List<MemberDto>> getAllByDepartment(@PathVariable("id") Long id) throws Exception{
        List<MemberDto> memberList = memberService.getAllByDepartment(id);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }

    @GetMapping("/academic-title/{id}")
    public ResponseEntity<List<MemberDto>> getAllByAcademicTitle(@PathVariable("id") Long id) throws Exception{
        List<MemberDto> memberList = memberService.getAllByAcademicTitle(id);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
    @GetMapping("/education-title/{id}")
    public ResponseEntity<List<MemberDto>> getAllByEducationTitle(@PathVariable("id") Long id) throws Exception{
        List<MemberDto> memberList = memberService.getAllByEducationTitle(id);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
    @GetMapping("/scientific-field/{id}")
    public ResponseEntity<List<MemberDto>> getAllByScientificField(@PathVariable("id") Long id) throws Exception{
        List<MemberDto> memberList = memberService.getAllByScientificField(id);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
}
