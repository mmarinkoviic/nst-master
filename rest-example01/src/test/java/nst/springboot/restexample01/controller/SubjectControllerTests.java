package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.service.SubjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(SubjectController.class)
public class SubjectControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectService subjectService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for saving a subject")
    public void saveTest() throws Exception {
        SubjectDto subjectDto = new SubjectDto(1L,"Subject", 6, "Department");
        when(subjectService.save(anyString(),anyInt(),anyString())).thenReturn(subjectDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/subject")
                        .param("Subject Name", subjectDto.getName())
                        .param("ESPB", String.valueOf(subjectDto.getEsbp()))
                        .param("Department Name", subjectDto.getDepartment())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(subjectDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for getting all subjects")
    public void getAllTest()throws Exception{
        SubjectDto subjectDto = new SubjectDto(1L,"Subject", 6, "Department");
        SubjectDto subjectDto2 = new SubjectDto(2L,"Subject 2", 7, "Department");
        List<SubjectDto> expected = Arrays.asList(subjectDto, subjectDto2);
        when(subjectService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/subject")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(subjectDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(subjectDto2.getName()));
    }
    @Test
    @DisplayName("Test for deleting subject")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/subject")
                        .param("ID", idDelete.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Subject removed!"));
    }
    @Test
    @DisplayName("Test for getting subject by id")
    public void findByIdTest() throws Exception{
        SubjectDto subjectDto = new SubjectDto(1L, "Subject", 6, "Department");
        when(subjectService.findById(subjectDto.getId())).thenReturn(subjectDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/subject/{id}", subjectDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(subjectDto.getName()));
    }
    @Test
    @DisplayName("Test for updating subject")
    public void updateTest() throws Exception {
        SubjectDto subjectDto = new SubjectDto(1L, "Subject", 6, "Department");

        mockMvc.perform(MockMvcRequestBuilders.patch("/subject/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(subjectDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Subject updated!"));
    }
    @Test
    @DisplayName("Test for getting all subject from same department")
    public void getByDepartmentIdTest() throws Exception {
        SubjectDto subjectDto = new SubjectDto(1L,"Subject", 6, "Department");
        SubjectDto subjectDto2 = new SubjectDto(2L,"Subject 2", 7, "Department");
        List<SubjectDto> expected = Arrays.asList(subjectDto, subjectDto2);

        when(subjectService.findByDepartment(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/subject/department/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(subjectDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(subjectDto2.getName()));
    }
}
