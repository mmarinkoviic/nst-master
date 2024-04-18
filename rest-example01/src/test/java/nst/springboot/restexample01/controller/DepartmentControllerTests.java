package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.DepartmentDto;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.service.DepartmentService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for saving a department")
    public void saveTest() throws Exception {
        DepartmentDto departmentDto = new DepartmentDto(1L,"Department");
        when(departmentService.save(anyString())).thenReturn(departmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/department")
                        .param("Department Name", departmentDto.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(departmentDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for getting all departments")
    public void getAllTest()throws Exception{
        DepartmentDto departmentDto = new DepartmentDto(1L,"Department");
        DepartmentDto departmentDto2 = new DepartmentDto(1L,"Department 2");
        List<DepartmentDto> expected = Arrays.asList(departmentDto, departmentDto2);
        when(departmentService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(departmentDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(departmentDto2.getName()));
    }
    @Test
    @DisplayName("Test for getting department by id")
    public void findByIdTest() throws Exception{
        DepartmentDto departmentDto = new DepartmentDto(1L,"Department");
        when(departmentService.findById(departmentDto.getId())).thenReturn(departmentDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", departmentDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(departmentDto.getName()));
    }
    @Test
    @DisplayName("Test for updating department")
    public void updateTest() throws Exception {
        DepartmentDto departmentDto = new DepartmentDto(1L,"Department");
        mockMvc.perform(MockMvcRequestBuilders.patch("/department/update/{id}", departmentDto.getId())
                        .param("Department", departmentDto.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(departmentDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Department updated!"));
    }
    @Test
    @DisplayName("Test for deleting department")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/department")
                        .param("ID", idDelete.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Department with id " + idDelete + " removed!"));
    }
    @Test
    @DisplayName("Test for getting secretary of department")
    public void findSecretaryTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L, "Mary", "Adams", "Academic Titile","Education Title", "Scientific Field", "Department");
        when(departmentService.getSecretary(1L)).thenReturn(memberDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/secretary", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(memberDto.getId()));
    }
    @Test
    @DisplayName("Test for getting handler of department")
    public void findHandlerTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L, "Mary", "Adams", "Academic Titile","Education Title", "Scientific Field", "Department");
        when(departmentService.getHandler(1L)).thenReturn(memberDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/handler", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(memberDto.getId()));
    }
    @Test
    @DisplayName("Test for putting secretary of department")
    public void putSecretaryTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/department/{id}/updateSecretary", 1L)
                        .param("memberId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Secretary posted!"));
    }
    @Test
    @DisplayName("Test for putting handler of department")
    public void putHandlerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/department/{id}/updateHandler", 1L)
                        .param("memberId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Handler posted!"));
    }
    @Test
    @DisplayName("Test for getting history of handlers of department")
    public void findHandlerHistory() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"handler", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));
        ManagementDto managementDto2 = new ManagementDto(2L, "Department",memberDto2, "handler", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(departmentService.getHandlerHistory(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/handler/history", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting history of secretaries of department")
    public void findSecretaryHistory() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"secretary", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));
        ManagementDto managementDto2 = new ManagementDto(2L, "Department",memberDto2, "secretary", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(departmentService.getSecretaryHistory(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/secretary/history", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all subjects from the same department")
    public void findSubjectsTest() throws Exception {
        SubjectDto subjectDto = new SubjectDto(1L,"Subject", 6, "Department");
        SubjectDto subjectDto2 = new SubjectDto(2L,"Subject 2", 7, "Department");
        List<SubjectDto> expected = Arrays.asList(subjectDto, subjectDto2);

        when(departmentService.getSubjects(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/subjects", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(subjectDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(subjectDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all members from the same department")
    public void findMembersTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");
        List<MemberDto> expected = Arrays.asList(memberDto, memberDto2);

        when(departmentService.getMembers(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/members", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(memberDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(memberDto2.getId()));
    }
}
