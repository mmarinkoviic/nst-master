package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.AcademicTitleDto;
import nst.springboot.restexample01.service.AcademicTitleService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(AcademicTitleController.class)
public class AcademicTitleControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AcademicTitleService academicTitleService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for saving an academic title")
    public void saveTest() throws Exception {
        AcademicTitleDto academicTitleDto = new AcademicTitleDto(1L,"Academic Title");
        when(academicTitleService.save(anyString())).thenReturn(academicTitleDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/academic-title")
                        .param("Academic title", academicTitleDto.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(academicTitleDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for getting all academic titles")
    public void getAllTest()throws Exception{
        AcademicTitleDto academicTitleDto = new AcademicTitleDto(1L,"Academic Title");
        AcademicTitleDto academicTitleDto2 = new AcademicTitleDto(2L,"Academic Title 2");
        List<AcademicTitleDto> expected = Arrays.asList(academicTitleDto, academicTitleDto2);
        when(academicTitleService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/academic-title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(academicTitleDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(academicTitleDto2.getTitle()));

    }
    @Test
    @DisplayName("Test for deleting academic title")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/academic-title")
                        .param("ID", idDelete.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Academic title with id " + idDelete + " removed!"));
    }
    @Test
    @DisplayName("Test for getting academic title by id")
    public void findByIdTest() throws Exception{
        AcademicTitleDto academicTitleDto = new AcademicTitleDto(1L, "Academic Title");
        when(academicTitleService.findById(academicTitleDto.getId())).thenReturn(academicTitleDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/academic-title/{id}", academicTitleDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(academicTitleDto.getTitle()));
    }
    @Test
    @DisplayName("Test for updating academic title")
    public void updateTest() throws Exception {
        Long idToUpdate = 1L;
        String newName = "New Academic Title";

        mockMvc.perform(MockMvcRequestBuilders.patch("/academic-title/update/{id}", idToUpdate)
                        .param("New name", newName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Academic title updated!"));
    }
}
