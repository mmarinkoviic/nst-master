package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.EducationTitleDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.service.AcademicTitleHistoryService;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(AcademicTitleHistoryController.class)
public class AcademicTitleHistoryControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AcademicTitleHistoryService academicTitleHistoryService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for getting all academic title histories")
    public void getAllTest()throws Exception{
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(1L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,5,5),LocalDate.of(2023,8,8),"Academic Title","Scientific Field");
        AcademicTitleHistoryDto academicTitleHistoryDto2 = new AcademicTitleHistoryDto(2L,new MemberDto(2L,"Tom","Savo","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,3,5),LocalDate.of(2023,6,8),"Academic Title","Scientific Field");
        List<AcademicTitleHistoryDto> expected = Arrays.asList(academicTitleHistoryDto, academicTitleHistoryDto2);

        when(academicTitleHistoryService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/academic-title-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(academicTitleHistoryDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(academicTitleHistoryDto2.getId()));
    }
    @Test
    @DisplayName("Test for deleting academic title history")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/academic-title-history")
                        .param("ID", idDelete.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Record removed!"));
    }
    @Test
    @DisplayName("Test for getting academic title history by id")
    public void findByIdTest() throws Exception{
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(1L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,5,5),LocalDate.of(2023,8,8),"Academic Title","Scientific Field");
        when(academicTitleHistoryService.findById(academicTitleHistoryDto.getId())).thenReturn(academicTitleHistoryDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/academic-title-history/{id}", academicTitleHistoryDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(academicTitleHistoryDto.getId()));
    }
    @Test
    @DisplayName("Test for getting all academic title history for same member")
    public void getAllByMember() throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(1L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,6,9),LocalDate.of(2023,8,8),"Academic Title","Scientific Field");
        AcademicTitleHistoryDto academicTitleHistoryDto2 = new AcademicTitleHistoryDto(2L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,3,5),LocalDate.of(2023,6,8),"Academic Title 2","Scientific Field");
        List<AcademicTitleHistoryDto> expected = Arrays.asList(academicTitleHistoryDto, academicTitleHistoryDto2);

        when(academicTitleHistoryService.getAllByMember(1L)).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/academic-title-history/member/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(academicTitleHistoryDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(academicTitleHistoryDto2.getId()));
    }
    @Test
    @DisplayName("Test for saving a academic title history")
    public void saveTest() throws Exception {
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(1L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,6,9),null,"Academic Title","Scientific Field");
        when(academicTitleHistoryService.save(anyLong(),any(LocalDate.class),anyString())).thenReturn(academicTitleHistoryDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/academic-title-history/save")
                        .param("Id Member", String.valueOf(academicTitleHistoryDto.getId()))
                        .param("Academic Title", academicTitleHistoryDto.getAcademicTitle())
                        .param("Start Date", academicTitleHistoryDto.getStartDate().format(DateTimeFormatter.ISO_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for saving previous academic title history")
    public void savePreviousTest()throws Exception{
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(1L,new MemberDto(1L,"Mary","Alice","Academic Title","Education Title","Scientific Field","Department"), LocalDate.of(2023,6,9),LocalDate.of(2023,8,8),"Academic Title","Scientific Field");
        when(academicTitleHistoryService.savePrevious(anyLong(),any(LocalDate.class),any(LocalDate.class),anyString())).thenReturn(academicTitleHistoryDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/academic-title-history/save-previous")
                        .param("Id Member", String.valueOf(academicTitleHistoryDto.getId()))
                        .param("Academic Title", academicTitleHistoryDto.getAcademicTitle())
                        .param("Start Date", academicTitleHistoryDto.getStartDate().format(DateTimeFormatter.ISO_DATE))
                        .param("End Date", academicTitleHistoryDto.getEndDate().format(DateTimeFormatter.ISO_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
