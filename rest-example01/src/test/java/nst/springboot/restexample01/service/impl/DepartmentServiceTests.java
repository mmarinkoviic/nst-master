package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.DepartmentConverter;
import nst.springboot.restexample01.converter.impl.ManagementConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.converter.impl.SubjectConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.DepartmentDto;
import nst.springboot.restexample01.dto.ManagementDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.ManagementRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.repository.SubjectRepository;
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

public class DepartmentServiceTests {
    @InjectMocks
    private DepartmentServiceImpl departmentService;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DepartmentConverter departmentConverter;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SubjectConverter subjectConverter;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberConverter memberConverter;
    @Mock
    private ManagementRepository managementRepository;
    @Mock
    private ManagementConverter managementConverter;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getAllTest(){
        Department department1 = new Department(20L,"Department 20");
        Department department2 = new Department(21L,"Department 21");

        DepartmentDto departmentDto1 = new DepartmentDto(department1.getId(),department1.getName());
        DepartmentDto departmentDto2 = new DepartmentDto(department2.getId(),department2.getName());

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department1, department2));
        when(departmentConverter.toDto(department1)).thenReturn(departmentDto1);
        when(departmentConverter.toDto(department2)).thenReturn(departmentDto2);

        List<DepartmentDto> returnedList = departmentService.getAll();

        verify(departmentRepository,times(1)).findAll();
        verify(departmentConverter,times(1)).toDto(department1);
        verify(departmentConverter,times(1)).toDto(department2);

        assertEquals(2,returnedList.size());
        assertEquals(departmentDto1,returnedList.get(0));
        assertEquals(departmentDto2,returnedList.get(1));
    }
    @Test
    public void findByIdTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        DepartmentDto departmentDto = new DepartmentDto(department.getId(),department.getName());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentConverter.toDto(department)).thenReturn(departmentDto);

        DepartmentDto returned = departmentService.findById(department.getId());

        verify(departmentRepository,times(1)).findById(department.getId());
        verify(departmentConverter,times(1)).toDto(department);

        assertEquals(departmentDto,returned);
    }
    @Test
    public void findByIdFailureTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> departmentService.findById(1L));
    }
    @Test
    public void saveTest() throws Exception{
        String newName = "Department New";

        when(departmentRepository.findByNameIgnoreCase(newName)).thenReturn(Optional.empty());
        when(departmentRepository.findMaxId()).thenReturn(154L);

        Department department = new Department(154L,newName);
        when(departmentRepository.save(any())).thenReturn(department);
        DepartmentDto expected = new DepartmentDto(154L,newName);
        when(departmentConverter.toDto(any())).thenReturn(expected);

        DepartmentDto returned = departmentService.save(newName);
        assertEquals(expected,returned);
    }
    @Test
    public void saveExistingNameTest(){
        String newName = "Department Existing";
        Department department = new Department(28L,newName);
        when(departmentRepository.findByNameIgnoreCase(newName)).thenReturn(Optional.of(department));

        assertThrows(EntityExistsException.class,()->departmentService.save(newName));
    }
    @Test
    public void updateTest() throws Exception {
        String updateName = "Updated Department";
        Department oldDepartment = new Department(26L,"Department");
        Department updatedDepartment = new Department(oldDepartment.getId(),updateName);

        when(departmentRepository.findByNameIgnoreCase(updateName)).thenReturn(Optional.empty());
        when(departmentRepository.findById(oldDepartment.getId())).thenReturn(Optional.of(oldDepartment));
        when(departmentRepository.save(updatedDepartment)).thenReturn(updatedDepartment);

        departmentService.update(oldDepartment.getId(),updateName);

        verify(departmentRepository,times(1)).save(any());
    }
    @Test
    public void updateExistingNameTest() throws Exception {
        String updateName = "Updated Department";
        Department oldDepartment = new Department(26L,updateName);

        when(departmentRepository.findByNameIgnoreCase(updateName)).thenReturn(Optional.of(oldDepartment));

        assertThrows(EntityExistsException.class,()->departmentService.update(1L,updateName));
    }
    @Test
    public void updateNotExistingTest() throws Exception {
        String updateName = "Updated Department";
        Department oldDepartment = new Department(26L,updateName);

        when(departmentRepository.findByNameIgnoreCase(updateName)).thenReturn(Optional.empty());
        when(departmentRepository.findById(oldDepartment.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->departmentService.update(1L,updateName));
    }
    @Test
    public void deleteTest() throws Exception {
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(subjectRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());
        when(memberRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());

        departmentService.delete(department.getId());
        verify(departmentRepository,times(1)).delete(department);
    }
    @Test
    public void deleteNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.delete(1L));
    }
    @Test
    public void deleteExistingMemberTest(){
        Department department = new Department(22L,"Department 22");
        Member member = new Member(22L,"Lucy","Parker",new AcademicTitle(1L,"AT1"),new EducationTitle(1L,"ET1"),new ScientificField(1L,"ScF1"),department);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(member));

        assertThrows(Exception.class,()->departmentService.delete(1L));
    }
    @Test
    public void deleteExistingSubjectTest(){
        Department department = new Department(22L,"Department 22");
        Subject subject = new Subject(1L,"Subject",6,department);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(subjectRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(subject));

        assertThrows(Exception.class,()->departmentService.delete(1L));
    }
    @Test
    public void getSubjectsTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        Subject subject1 = new Subject(1L,"Subject",6,department);
        Subject subject2 = new Subject(2L,"Subject 2",6,department);
        SubjectDto subjectDto1 = new SubjectDto(subject1.getId(),subject1.getName(),subject1.getEsbp(),subject1.getDepartment().getName());
        SubjectDto subjectDto2 = new SubjectDto(subject2.getId(),subject2.getName(),subject2.getEsbp(),subject2.getDepartment().getName());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(subjectRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(subject1,subject2));
        when(subjectConverter.toDto(subject1)).thenReturn(subjectDto1);
        when(subjectConverter.toDto(subject2)).thenReturn(subjectDto2);

        List<SubjectDto> returnedList = departmentService.getSubjects(department.getId());
        assertEquals(2,returnedList.size());
        assertEquals(subjectDto1,returnedList.get(0));
        assertEquals(subjectDto2,returnedList.get(1));
    }
    @Test
    public void getSubjectsNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.getSubjects(1L));
    }
    @Test
    public void getSubjectsEmptyTest(){
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(subjectRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,()->departmentService.getSubjects(department.getId()));
    }
    @Test
    public void getMembersTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member1 = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(23L,"Rene","Frank",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(member1,member2));
        when(memberConverter.toDto(member1)).thenReturn(memberDto1);
        when(memberConverter.toDto(member2)).thenReturn(memberDto2);

        List<MemberDto> returnedList = departmentService.getMembers(department.getId());
        assertEquals(2,returnedList.size());
        assertEquals(memberDto1,returnedList.get(0));
        assertEquals(memberDto2,returnedList.get(1));
    }
    @Test
    public void getMembersNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.getMembers(1L));
    }
    @Test
    public void getMembersEmptyTest(){
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,()->departmentService.getMembers(department.getId()));
    }
    @Test
    public void getSecretaryTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        Management management = new Management(22L, department, member, "secretary",LocalDate.of(2023,5,5),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.of(management));
        when(memberConverter.toDto(management.getMember())).thenReturn(memberDto);

        MemberDto returned = departmentService.getSecretary(department.getId());

        assertEquals(memberDto,returned);
    }
    @Test
    public void getSecretaryNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.getSecretary(1L));
    }
    @Test
    public void getSecretaryEmptyTest() throws Exception {
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->departmentService.getSecretary(department.getId()));
    }
    @Test
    public void getHandlerTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        Management management = new Management(22L, department, member, "handler",LocalDate.of(2023,5,5),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.of(management));
        when(memberConverter.toDto(management.getMember())).thenReturn(memberDto);

        MemberDto returned = departmentService.getHandler(department.getId());

        assertEquals(memberDto,returned);
    }
    @Test
    public void getHandlerNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.getHandler(1L));
    }
    @Test
    public void getHandlerEmptyTest() throws Exception {
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->departmentService.getHandler(department.getId()));
    }
    @Test
    public void getSecretaryHistoryTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        Member member2 = new Member(23L,"Becky","Frank",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());
        Management management = new Management(22L, department, member, "secretary",LocalDate.of(2023,5,5),null);
        ManagementDto managementDto = new ManagementDto(management.getId(), management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(22L, department, member2, "secretary",LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        ManagementDto managementDto2 = new ManagementDto(management2.getId(), management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returned = departmentService.getSecretaryHistory(department.getId());

        assertEquals(2,returned.size());
        assertEquals(managementDto,returned.get(0));
        assertEquals(managementDto2,returned.get(1));
    }
    @Test
    public void getSecretaryHistoryNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.getSecretaryHistory(1L));
    }
    @Test
    public void getSecretaryHistoryEmptyTest() throws Exception {
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,()->departmentService.getSecretaryHistory(department.getId()));
    }
    @Test
    public void getHandlerHistoryTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        Member member2 = new Member(23L,"Becky","Frank",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());
        Management management = new Management(22L, department, member, "handler",LocalDate.of(2023,5,5),null);
        ManagementDto managementDto = new ManagementDto(management.getId(), management.getDepartment().getName(),memberDto,management.getRole(),management.getStartDate(),management.getEndDate());
        Management management2 = new Management(22L, department, member2, "handler",LocalDate.of(2023,3,3),LocalDate.of(2023,5,5));
        ManagementDto managementDto2 = new ManagementDto(management2.getId(), management2.getDepartment().getName(),memberDto2,management2.getRole(),management2.getStartDate(),management2.getEndDate());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Arrays.asList(management,management2));
        when(managementConverter.toDto(management)).thenReturn(managementDto);
        when(managementConverter.toDto(management2)).thenReturn(managementDto2);

        List<ManagementDto> returned = departmentService.getHandlerHistory(department.getId());

        assertEquals(2,returned.size());
        assertEquals(managementDto,returned.get(0));
        assertEquals(managementDto2,returned.get(1));
    }
    @Test
    public void getHandlerHistoryNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.getHandlerHistory(1L));
    }
    @Test
    public void getHandlerHistoryEmptyTest() throws Exception {
        Department department = new Department(22L,"Department 22");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(managementRepository.findByDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,()->departmentService.getHandlerHistory(department.getId()));
    }
    @Test
    public void putSecretaryTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "secretary",LocalDate.of(2023,5,5),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "secretary")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.empty());
        when(managementRepository.count()).thenReturn(Long.valueOf(21));
        when(managementRepository.save(management)).thenReturn(management);

        departmentService.putSecretary(department.getId(), member.getId());

        verify(managementRepository,times(1)).save(any());
    }
    @Test
    public void putSecretaryNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.putSecretary(1L,1L));
    }
    @Test
    public void putSecretaryNotExistingMemberTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.putSecretary(1L,1L));
    }
    @Test
    public void putSecretaryAlreadyHandlerTest(){
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "secretary",LocalDate.of(2023,5,5),null);
        Management management2 = new Management(22L, department, member, "handler",LocalDate.of(2023,3,3),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "secretary")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.of(management2));

        assertThrows(Exception.class,()->departmentService.putSecretary(department.getId(), member.getId()));
    }
    @Test
    public void putSecretaryAlreadySecretaryTest(){
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "secretary",LocalDate.of(2023,5,5),null);
        Management management2 = new Management(22L, department, member, "handler",LocalDate.of(2023,3,3),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "secretary")).thenReturn(Optional.of(management));

        assertThrows(Exception.class,()->departmentService.putSecretary(department.getId(), member.getId()));
    }
    @Test
    public void putSecretaryAlreadyHaveSecretaryTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(23L,"Becky","Frank",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "secretary",LocalDate.of(2023,5,5),null);
        Management management2 = new Management(22L, department, member2, "secretary",LocalDate.of(2023,3,3),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "secretary")).thenReturn(Optional.of(management2));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"handler")).thenReturn(Optional.empty());
        when(managementRepository.count()).thenReturn(Long.valueOf(21));
        when(managementRepository.save(management)).thenReturn(management);

        departmentService.putSecretary(department.getId(), member.getId());

        verify(managementRepository,times(2)).save(any());
    }
    @Test
    public void putHandlerTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "handler",LocalDate.of(2023,5,5),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "handler")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.empty());
        when(managementRepository.count()).thenReturn(Long.valueOf(21));
        when(managementRepository.save(management)).thenReturn(management);

        departmentService.putHandler(department.getId(), member.getId());

        verify(managementRepository,times(1)).save(any());
    }
    @Test
    public void putHandlerNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.putHandler(1L,1L));
    }
    @Test
    public void putHandlerNotExistingMemberTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->departmentService.putHandler(1L,1L));
    }
    @Test
    public void putHandlerAlreadySecretaryTest(){
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Management management2 = new Management(22L, department, member, "secretary",LocalDate.of(2023,3,3),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "handler")).thenReturn(Optional.empty());
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.of(management2));

        assertThrows(Exception.class,()->departmentService.putHandler(department.getId(), member.getId()));
    }
    @Test
    public void putHandlerAlreadyHandlerTest(){
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "handler",LocalDate.of(2023,5,5),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "handler")).thenReturn(Optional.of(management));

        assertThrows(Exception.class,()->departmentService.putHandler(department.getId(), member.getId()));
    }
    @Test
    public void putHandlerAlreadyHaveHandlerTest() throws Exception {
        Department department = new Department(22L,"Department 22");
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        Member member = new Member(22L,"Anna","Jure",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(23L,"Becky","Frank",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(22L, department, member, "handler",LocalDate.of(2023,5,5),null);
        Management management2 = new Management(22L, department, member2, "handler",LocalDate.of(2023,3,3),null);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(), "handler")).thenReturn(Optional.of(management2));
        when(managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department.getId(),"secretary")).thenReturn(Optional.empty());
        when(managementRepository.count()).thenReturn(Long.valueOf(21));
        when(managementRepository.save(management)).thenReturn(management);

        departmentService.putHandler(department.getId(), member.getId());

        verify(managementRepository,times(2)).save(any());
    }
}
