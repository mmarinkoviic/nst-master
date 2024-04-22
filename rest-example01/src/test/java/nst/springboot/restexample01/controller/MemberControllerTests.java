package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberController.class)
public class MemberControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for getting all members")
    public void getAllTest()throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");
        List<MemberDto> expected = Arrays.asList(memberDto, memberDto2);
        when(memberService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/member")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(memberDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(memberDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting member by id")
    public void findByIdTest() throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        when(memberService.findById(memberDto.getId())).thenReturn(memberDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/member/{id}", memberDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(memberDto.getId()));
    }
    @Test
    @DisplayName("Test for deleting member")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/member")
                        .param("ID", idDelete.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Member removed!"));
    }
    @Test
    @DisplayName("Test for saving a member")
    public void saveTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        when(memberService.save(anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(memberDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/member")
                        .param("First Name", memberDto.getFirstName())
                        .param("Last Name", memberDto.getLastName())
                        .param("Academic Title", memberDto.getAcademicTitle())
                        .param("Education Title", memberDto.getEducationTitle())
                        .param("Scientific Field", memberDto.getScientificField())
                        .param("Department", memberDto.getDepartment())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for updating member")
    public void updateTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");

        mockMvc.perform(MockMvcRequestBuilders.patch("/member/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Member updated!"));
    }
    @Test
    @DisplayName("Test for getting academic title history of member")
    public void getHistory() throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(1L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,6,9),LocalDate.of(2023,8,8),"Academic Title","Scientific Field");
        AcademicTitleHistoryDto academicTitleHistoryDto2 = new AcademicTitleHistoryDto(2L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,3,5),LocalDate.of(2023,6,8),"Academic Title 2","Scientific Field");
        List<AcademicTitleHistoryDto> expected = Arrays.asList(academicTitleHistoryDto, academicTitleHistoryDto2);

        when(memberService.getHistory(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/member/{id}/academic-title-history", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(academicTitleHistoryDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(academicTitleHistoryDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all members from the same department")
    public void getAllByDepartmentTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");
        List<MemberDto> expected = Arrays.asList(memberDto, memberDto2);
        when(memberService.getAllByDepartment(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/member/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(memberDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(memberDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all members with the same academic title")
    public void getAllByAcademicTitleTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");
        List<MemberDto> expected = Arrays.asList(memberDto, memberDto2);
        when(memberService.getAllByAcademicTitle(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/member/academic-title/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(memberDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(memberDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all members with the same education title")
    public void getAllByEducationTitleTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");
        List<MemberDto> expected = Arrays.asList(memberDto, memberDto2);
        when(memberService.getAllByEducationTitle(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/member/education-title/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(memberDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(memberDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all members from the same scientific field")
    public void getAllByScientificFieldTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");
        List<MemberDto> expected = Arrays.asList(memberDto, memberDto2);
        when(memberService.getAllByScientificField(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/member/scientific-field/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(memberDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(memberDto2.getId()));
    }
}
