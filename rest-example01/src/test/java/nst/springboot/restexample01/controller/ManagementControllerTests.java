package nst.springboot.restexample01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.service.ManagementService;
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

@WebMvcTest(ManagementController.class)
public class ManagementControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ManagementService managementService;
    private String asJsonString(Object o) throws Exception{
        return new ObjectMapper().writeValueAsString(o);
    }
    @Test
    @DisplayName("Test for saving a management record")
    public void saveTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"handler", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));

        when(managementService.save(anyLong(),anyLong(),anyString(),any(LocalDate.class))).thenReturn(managementDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/management")
                        .param("Department ID", String.valueOf(1L))
                        .param("Member ID", String.valueOf(1L))
                        .param("Role", "handler")
                        .param("Start Date", managementDto.getStartDate().format(DateTimeFormatter.ISO_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    @DisplayName("Test for getting all management records")
    public void getAllTest()throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"secretary", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));
        ManagementDto managementDto2 = new ManagementDto(2L, "Department",memberDto2, "secretary", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.getAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/management")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting management record by id")
    public void findByIdTest() throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"handler", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));

        when(managementService.findById(managementDto.getId())).thenReturn(managementDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/management/{id}", managementDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(managementDto.getId()));
    }

    @Test
    @DisplayName("Test for deleting management record")
    public void deleteTest() throws Exception{
        Long idDelete = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/management")
                        .param("ID", idDelete.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Record removed!"));
    }
    @Test
    @DisplayName("Test for getting all management record about department")
    public void historyOfDepartmentTest() throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"secretary", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));
        ManagementDto managementDto2 = new ManagementDto(2L, "Department",memberDto2, "secretary", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.findByDepartment(1L)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/department/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting current management record about department")
    public void managementOfDepartmentTest() throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"secretary", LocalDate.of(2023,5,5), null);
        ManagementDto managementDto2 = new ManagementDto(2L, "Department",memberDto2, "handler", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.findCurrentDepartment(1L)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/department/{id}/current", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting management record about current handlers")
    public void currentHandlersTest() throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"handler", LocalDate.of(2023,5,5), null);
        ManagementDto managementDto2 = new ManagementDto(2L, "Department 2",memberDto2, "handler", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.currentHandlers()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/handlers/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting management record about current secretaries")
    public void currentSecretariesTest() throws Exception{
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"secretary", LocalDate.of(2023,5,5), null);
        ManagementDto managementDto2 = new ManagementDto(2L, "Department 2",memberDto2, "secretary", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.currentSecretary()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/secretaries/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all management records about handlers")
    public void getHandlersTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"handler", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));
        ManagementDto managementDto2 = new ManagementDto(2L, "Department 2",memberDto2, "handler", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.getHandlers()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/handlers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
    @Test
    @DisplayName("Test for getting all management records about secretaries")
    public void getSecretariesTest() throws Exception {
        MemberDto memberDto = new MemberDto(1L,"Mary", "Wolt", "Academic Title", "Education Title", "Scientific field", "Department");
        MemberDto memberDto2 = new MemberDto(2L,"Anton", "Pery", "Academic Title", "Education Title", "Scientific field", "Department");

        ManagementDto managementDto = new ManagementDto(1L, "Department", memberDto,"secretary", LocalDate.of(2023,5,5), LocalDate.of(2023,12,12));
        ManagementDto managementDto2 = new ManagementDto(2L, "Department 2",memberDto2, "secretary", LocalDate.of(2023,12,12),null);

        List<ManagementDto> expected = Arrays.asList(managementDto, managementDto2);
        when(managementService.getSecretaries()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/secretaries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(managementDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(managementDto2.getId()));
    }
}
