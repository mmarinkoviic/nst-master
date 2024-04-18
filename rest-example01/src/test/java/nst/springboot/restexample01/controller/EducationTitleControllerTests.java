package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.EducationTitleDto;
import nst.springboot.restexample01.service.EducationTitleService;
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


@WebMvcTest(EducationTitleController.class)
public class EducationTitleControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EducationTitleService educationTitleService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for saving an education title")
    public void saveTest() throws Exception {
        EducationTitleDto educationTitleDto = new EducationTitleDto(1L,"Education Title");
        when(educationTitleService.save(anyString())).thenReturn(educationTitleDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/education-title")
                        .param("Education title", "Education Title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(educationTitleDto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for getting all education titles")
    public void getAllTest()throws Exception{
        EducationTitleDto educationTitleDto = new EducationTitleDto(1L,"Education Title");
        EducationTitleDto educationTitleDto2 = new EducationTitleDto(2L,"Education Title 2");
        List<EducationTitleDto> expected = Arrays.asList(educationTitleDto, educationTitleDto2);
        when(educationTitleService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/education-title")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(educationTitleDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(educationTitleDto2.getTitle()));

    }
    @Test
    @DisplayName("Test for deleting education title")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/education-title")
                .param("ID", idDelete.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Education title with id " + idDelete + " removed!"));
    }
    @Test
    @DisplayName("Test for getting education title by id")
    public void findByIdTest() throws Exception{
        EducationTitleDto educationTitleDto = new EducationTitleDto(1L, "Education Title");
        when(educationTitleService.findById(educationTitleDto.getId())).thenReturn(educationTitleDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/education-title/{id}",educationTitleDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(educationTitleDto.getTitle()));
    }
    @Test
    @DisplayName("Test for updating education title")
    public void updateTest() throws Exception {
        Long idToUpdate = 1L;
        String newName = "New Education Title";

        mockMvc.perform(MockMvcRequestBuilders.patch("/education-title/update/{id}", idToUpdate)
                        .param("New name", newName)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("Education title updated!"));
    }
}
