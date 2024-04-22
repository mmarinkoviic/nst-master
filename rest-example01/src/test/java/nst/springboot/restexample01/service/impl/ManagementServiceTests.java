package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.ManagementConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.ManagementRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ManagementServiceTests {
    @InjectMocks
    private ManagementServiceImpl managementService;
    @Mock
    private ManagementRepository managementRepository;
    @Mock
    private ManagementConverter managementConverter;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getAllTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Management management1 = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        Management management2 = new Management(25L,department,member,"secretary" ,LocalDate.now(),null);

        when(managementRepository.findAll()).thenReturn(Arrays.asList(management1, management2));
        when(managementConverter.toDto(management1)).thenReturn(new ManagementDto(management1.getId(),management1.getDepartment().getName(),memberDto,management1.getRole(),management1.getStartDate(),management1.getEndDate()));
        when(managementConverter.toDto(management2)).thenReturn(new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto,management2.getRole(),management2.getStartDate(),management2.getEndDate()));

        List<ManagementDto> returnedList = managementService.getAll();

        verify(managementRepository,times(1)).findAll();
        verify(managementConverter,times(1)).toDto(management1);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(management1.getId(),returnedList.get(0).getId());
        assertEquals(management2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void findByIdTest() throws Exception {
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());

        when(managementRepository.findById(management.getId())).thenReturn(Optional.of(management));
        when(managementConverter.toDto(management)).thenReturn(managementDto);

        ManagementDto returned = managementService.findById(management.getId());

        verify(managementRepository,times(1)).findById(management.getId());
        verify(managementConverter,times(1)).toDto(management);

        assertEquals(managementDto, returned);
    }
    @Test
    public void findByIdFailureTest(){
        when(managementRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> managementService.findById(1L));
    }
    @Test
    public void deleteTest() throws Exception {
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));

        when(managementRepository.findById(management.getId())).thenReturn(Optional.of(management));

        managementService.delete(management.getId());
        verify(managementRepository,times(1)).delete(management);
    }
    @Test
    public void deleteNotFoundTest(){
        when(managementRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> managementService.delete(1L));
    }
    @Test
    public void deleteNotApprovedTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),null);

        when(managementRepository.findById(management.getId())).thenReturn(Optional.of(management));

        assertThrows(Exception.class,()-> managementService.delete(management.getId()));
    }
    @Test
    public void findByDepartmentTest() throws Exception {
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(25L,department,member,"secretary" ,LocalDate.of(2024,3,3),null);
        ManagementDto managementDto2 = new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        when(managementRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returnedList = managementService.findByDepartment(department.getId());

        verify(managementRepository,times(1)).findByDepartmentId(department.getId());
        verify(managementConverter,times(1)).toDto(management);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(managementDto,returnedList.get(0));
        assertEquals(managementDto2,returnedList.get(1));
    }
    @Test
    public void findByDepartmentNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> managementService.findByDepartment(1L));
    }
    @Test
    public void findByDepartmentEmptyListTest(){
        Department department = new Department(1L,"Department");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        when(managementRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> managementService.findByDepartment(1L));
    }
    @Test
    public void findCurrentDepartmentTest() throws Exception {
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member2 = new Member(25L,"Luke","Fredy",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto2 = new MemberDto(25L,"Luke","Fredy",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),null);
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(25L,department,member2,"handler" ,LocalDate.of(2024,3,3),null);
        ManagementDto managementDto2 = new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByEndDateIsNullAndDepartmentId(department.getId())).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returnedList = managementService.findCurrentDepartment(department.getId());

        verify(managementRepository,times(1)).findByEndDateIsNullAndDepartmentId(department.getId());
        verify(managementConverter,times(1)).toDto(management);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(managementDto,returnedList.get(0));
        assertEquals(managementDto2,returnedList.get(1));
    }
    @Test
    public void findCurrentDepartmentNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> managementService.findCurrentDepartment(1L));
    }
    @Test
    public void findCurrentDepartmentEmptyTest(){
        Department department = new Department(25L,"Department");
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByEndDateIsNullAndDepartmentId(department.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> managementService.findCurrentDepartment(department.getId()));
    }
    @Test
    public void currentHandlersTest() throws Exception {
        Department department = new Department(25L,"Department 25");
        Department department2 = new Department(26L,"Department 26");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member2 = new Member(25L,"Luke","Fredy",academicTitle,educationTitle,scientificField,department2);
        MemberDto memberDto2 = new MemberDto(25L,"Luke","Fredy",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        Management management = new Management(25L,department,member,"handler" ,LocalDate.of(2023,3,3),null);
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(25L,department2,member2,"handler" ,LocalDate.of(2024,3,3),null);
        ManagementDto managementDto2 = new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(managementRepository.findByEndDateIsNullAndRole("handler")).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returnedList = managementService.currentHandlers();

        verify(managementRepository,times(1)).findByEndDateIsNullAndRole("handler");
        verify(managementConverter,times(1)).toDto(management);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(managementDto,returnedList.get(0));
        assertEquals(managementDto2,returnedList.get(1));
    }
    @Test
    public void currentHandlersEmptyTest(){
        when(managementRepository.findByEndDateIsNullAndRole("handler")).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> managementService.currentHandlers());
    }
    @Test
    public void getHandlersTest() throws Exception{
        Department department = new Department(25L,"Department 25");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member2 = new Member(25L,"Luke","Fredy",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto2 = new MemberDto(25L,"Luke","Fredy",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        Management management = new Management(25L,department,member,"handler" ,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(25L,department,member2,"handler" ,LocalDate.of(2024,3,3),null);
        ManagementDto managementDto2 = new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(managementRepository.findByRole("handler")).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returnedList = managementService.getHandlers();

        verify(managementRepository,times(1)).findByRole("handler");
        verify(managementConverter,times(1)).toDto(management);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(managementDto,returnedList.get(0));
        assertEquals(managementDto2,returnedList.get(1));
    }
    @Test
    public void getHandlersNullTest(){
        when(managementRepository.findByRole("handler")).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> managementService.getHandlers());
    }
    @Test
    public void getSecretariesTest() throws Exception{
        Department department = new Department(25L,"Department 25");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member2 = new Member(25L,"Luke","Fredy",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto2 = new MemberDto(25L,"Luke","Fredy",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(25L,department,member2,"secretary" ,LocalDate.of(2024,3,3),null);
        ManagementDto managementDto2 = new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(managementRepository.findByRole("secretary")).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returnedList = managementService.getSecretaries();

        verify(managementRepository,times(1)).findByRole("secretary");
        verify(managementConverter,times(1)).toDto(management);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(managementDto,returnedList.get(0));
        assertEquals(managementDto2,returnedList.get(1));
    }
    @Test
    public void getSecretariesNullTest(){
        when(managementRepository.findByRole("secretary")).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> managementService.getSecretaries());
    }
    @Test
    public void currentSecretaryTest() throws Exception {
        Department department = new Department(25L,"Department 25");
        Department department2 = new Department(26L,"Department 26");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Member member2 = new Member(25L,"Luke","Fredy",academicTitle,educationTitle,scientificField,department2);
        MemberDto memberDto2 = new MemberDto(25L,"Luke","Fredy",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());

        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),null);
        ManagementDto managementDto = new ManagementDto(management.getId(),management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(25L,department2,member2,"secretary" ,LocalDate.of(2024,3,3),null);
        ManagementDto managementDto2 = new ManagementDto(management2.getId(),management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(managementRepository.findByEndDateIsNullAndRole("secretary")).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returnedList = managementService.currentSecretary();

        verify(managementRepository,times(1)).findByEndDateIsNullAndRole("secretary");
        verify(managementConverter,times(1)).toDto(management);
        verify(managementConverter,times(1)).toDto(management2);

        assertEquals(2,returnedList.size());
        assertEquals(managementDto,returnedList.get(0));
        assertEquals(managementDto2,returnedList.get(1));
    }
    @Test
    public void currentSecretaryEmptyTest(){
        when(managementRepository.findByEndDateIsNullAndRole("secretary")).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> managementService.currentSecretary());
    }
    @Test
    public void saveTest() throws Exception {
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),null);
        ManagementDto managementDto = new ManagementDto(25L,management.getDepartment().getName(),memberDto,management.getRole() ,management.getStartDate(),management.getEndDate());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findMaxId()).thenReturn(24L);
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),management.getRole())).thenReturn(Optional.empty());
        when(managementRepository.save(management)).thenReturn(management);
        when(managementConverter.toDto(management)).thenReturn(managementDto);

        ManagementDto returned = managementService.save(management.getDepartment().getId(),management.getMember().getId(),management.getRole(),management.getStartDate());
        assertEquals(managementDto,returned);
    }
    @Test
    public void saveInvalidRoleTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);

        assertThrows(IllegalArgumentException.class,()-> managementService.save(department.getId(),member.getId(),"teacher",LocalDate.of(2023,5,5)));
    }
    @Test
    public void saveNotExistingMemberTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> managementService.save(1L,1L,"handler",LocalDate.of(2023,5,5)));

    }
    @Test
    public void saveNotExistingDepartmentTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> managementService.save(1L,1L,"handler",LocalDate.of(2023,5,5)));

    }
    @Test
    public void saveIllegalDateTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        assertThrows(IllegalArgumentException.class,()-> managementService.save(department.getId(),member.getId(),"handler",LocalDate.of(2025,5,5)));
    }
    @Test
    public void saveExistingTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(25L,"Mery","Alice",academicTitle.getTitle(),educationTitle.getTitle(),scientificField.getScfField(),department.getName());
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),null);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findMaxId()).thenReturn(24L);
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),management.getRole())).thenReturn(Optional.of(management));

        assertThrows(EntityExistsException.class,()-> managementService.save(department.getId(),member.getId(),management.getRole(),LocalDate.of(2023,5,5)));
    }
    @Test
    public void saveAlreadyHandlerTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,3,3),null);
        Management management2 = new Management(25L,department,member,"handler" ,LocalDate.of(2023,2,3),null);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findMaxId()).thenReturn(24L);
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.of(management2));

        assertThrows(EntityExistsException.class,()-> managementService.save(department.getId(),member.getId(),management.getRole(),management.getStartDate()));
    }
    @Test
    public void saveAlreadySecretaryTest(){
        Department department = new Department(25L,"Department");
        AcademicTitle academicTitle = new AcademicTitle(25L,"Academic title 25");
        EducationTitle educationTitle = new EducationTitle(25L,"Education title 25");
        ScientificField scientificField = new ScientificField(25L, "Scientific field 25");
        Member member = new Member(25L,"Mery","Alice",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(25L,department,member,"handler" ,LocalDate.of(2023,3,3),null);
        Management management2 = new Management(25L,department,member,"secretary" ,LocalDate.of(2023,2,3),null);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findMaxId()).thenReturn(24L);
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.of(management2));

        assertThrows(EntityExistsException.class,()-> managementService.save(department.getId(),member.getId(),management.getRole(),management.getStartDate()));
    }

}
