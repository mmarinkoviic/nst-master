package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.ScientificFieldDto;
import nst.springboot.restexample01.service.ScientificFieldService;
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

@WebMvcTest(ScientificFieldController.class)
public class ScientificFieldControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScientificFieldService scientificFieldService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for saving an scientific field")
    public void saveTest() throws Exception {
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(1L,"Scientific Field");
        when(scientificFieldService.save(anyString())).thenReturn(scientificFieldDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/scientific-field")
                        .param("Scientific field", scientificFieldDto.getScfField())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scientificFieldDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for getting all scientific fields")
    public void getAllTest()throws Exception{
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(1L,"Scientific field");
        ScientificFieldDto scientificfieldDto2 = new ScientificFieldDto(2L,"Scientific field 2");
        List<ScientificFieldDto> expected = Arrays.asList(scientificFieldDto, scientificfieldDto2);
        when(scientificFieldService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/scientific-field")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scfField").value(scientificFieldDto.getScfField()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].scfField").value(scientificfieldDto2.getScfField()));
    }
    @Test
    @DisplayName("Test for deleting scientific field")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/scientific-field")
                        .param("ID", Long.toString(idDelete))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Scientific field with id " + idDelete + " removed!"));
    }
    @Test
    @DisplayName("Test for getting scientific field by id")
    public void findByIdTest() throws Exception{
        ScientificFieldDto scientificFieldDto = new ScientificFieldDto(1L, "Scientific Field");
        when(scientificFieldService.findById(scientificFieldDto.getId())).thenReturn(scientificFieldDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/scientific-field/{id}", scientificFieldDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.scfField").value(scientificFieldDto.getScfField()));
    }
    @Test
    @DisplayName("Test for updating scientific field")
    public void updateTest() throws Exception {
        Long idToUpdate = 1L;
        String newName = "New Scientific Field";

        mockMvc.perform(MockMvcRequestBuilders.patch("/scientific-field/update/{id}", idToUpdate)
                        .param("New name", newName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Scientific field updated!"));
    }
}
