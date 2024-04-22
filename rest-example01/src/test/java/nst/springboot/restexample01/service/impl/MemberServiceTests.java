package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.repository.*;
import nst.springboot.restexample01.service.AcademicTitleHistoryService;
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

public class MemberServiceTests {
    @InjectMocks
    public MemberServiceImpl memberService;
    @Mock
    public MemberRepository memberRepository;
    @Mock
    public MemberConverter memberConverter;
    @Mock
    public AcademicTitleRepository academicTitleRepository;
    @Mock
    public EducationTitleRepository educationTitleRepository;
    @Mock
    public ScientificFieldRepository scientificFieldRepository;
    @Mock
    public DepartmentRepository departmentRepository;
    @Mock
    public ManagementRepository managementRepository;
    @Mock
    public AcademicTitleHistoryRepository academicTitleHistoryRepository;
    @Mock
    public AcademicTitleHistoryConverter academicTitleHistoryConverter;
    @Mock
    public AcademicTitleHistoryService academicTitleHistoryService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTest(){
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");
        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(20L,"Petar","Pan",academicTitle,educationTitle,scientificField,department);

        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));
        when(memberConverter.toDto(member1)).thenReturn(new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName()));
        when(memberConverter.toDto(member2)).thenReturn(new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName()));

        List<MemberDto> returnedList = memberService.getAll();

        verify(memberRepository,times(1)).findAll();
        verify(memberConverter,times(1)).toDto(member1);
        verify(memberConverter,times(1)).toDto(member2);

        assertEquals(2,returnedList.size());
        assertEquals(member1.getId(),returnedList.get(0).getId());
        assertEquals(member2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void getAllByDepartmentTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(20L,"Petar","Pan",academicTitle,educationTitle,scientificField,department);

        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(memberRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(member1,member2));
        when(memberConverter.toDto(member1)).thenReturn(memberDto1);
        when(memberConverter.toDto(member2)).thenReturn(memberDto2);

        List<MemberDto> returnedList = memberService.getAllByDepartment(department.getId());
        verify(memberRepository,times(1)).findByDepartmentId(department.getId());
        verify(memberConverter,times(1)).toDto(member1);
        verify(memberConverter,times(1)).toDto(member2);

        assertEquals(2,returnedList.size());
        assertEquals(member1.getId(),returnedList.get(0).getId());
        assertEquals(member2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void getAllByDepartmentNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.getAllByDepartment(1L));
    }
    @Test
    public void getAllByDepartmentEmptyListTest(){
        Department department = new Department(1L,"Department");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        when(memberRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> memberService.getAllByDepartment(1L));
    }
    @Test
    public void getAllByAcademicTitleTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(20L,"Petar","Pan",academicTitle,educationTitle,scientificField,department);

        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());

        when(academicTitleRepository.findById(academicTitle.getId())).thenReturn(Optional.of(academicTitle));
        when(memberRepository.findByAcademicTitleId(academicTitle.getId())).thenReturn(Arrays.asList(member1,member2));
        when(memberConverter.toDto(member1)).thenReturn(memberDto1);
        when(memberConverter.toDto(member2)).thenReturn(memberDto2);

        List<MemberDto> returnedList = memberService.getAllByAcademicTitle(academicTitle.getId());
        verify(memberRepository,times(1)).findByAcademicTitleId(academicTitle.getId());
        verify(memberConverter,times(1)).toDto(member1);
        verify(memberConverter,times(1)).toDto(member2);

        assertEquals(2,returnedList.size());
        assertEquals(member1.getId(),returnedList.get(0).getId());
        assertEquals(member2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void getAllByAcademicTitleNotExistingTest(){
        when(academicTitleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.getAllByAcademicTitle(1L));
    }
    @Test
    public void getAllByAcademicTitleEmptyListTest(){
        AcademicTitle academicTitle = new AcademicTitle(1L,"Academic title");
        when(academicTitleRepository.findById(1L)).thenReturn(Optional.of(academicTitle));

        when(memberRepository.findByAcademicTitleId(academicTitle.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> memberService.getAllByAcademicTitle(1L));
    }
    @Test
    public void getAllByEducationTitleTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(20L,"Petar","Pan",academicTitle,educationTitle,scientificField,department);

        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());

        when(educationTitleRepository.findById(educationTitle.getId())).thenReturn(Optional.of(educationTitle));
        when(memberRepository.findByEducationTitleId(educationTitle.getId())).thenReturn(Arrays.asList(member1,member2));
        when(memberConverter.toDto(member1)).thenReturn(memberDto1);
        when(memberConverter.toDto(member2)).thenReturn(memberDto2);

        List<MemberDto> returnedList = memberService.getAllByEducationTitle(educationTitle.getId());
        verify(memberRepository,times(1)).findByEducationTitleId(educationTitle.getId());
        verify(memberConverter,times(1)).toDto(member1);
        verify(memberConverter,times(1)).toDto(member2);

        assertEquals(2,returnedList.size());
        assertEquals(member1.getId(),returnedList.get(0).getId());
        assertEquals(member2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void getAllByEducationTitleNotExistingTest(){
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.getAllByEducationTitle(1L));
    }
    @Test
    public void getAllByEducationTitleEmptyListTest(){
        EducationTitle educationTitle = new EducationTitle(1L,"Education title");
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.of(educationTitle));

        when(memberRepository.findByEducationTitleId(educationTitle.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> memberService.getAllByEducationTitle(1L));
    }
    @Test
    public void getAllByScientificFieldTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        Member member2 = new Member(20L,"Petar","Pan",academicTitle,educationTitle,scientificField,department);

        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());
        MemberDto memberDto2 = new MemberDto(member2.getId(),member2.getFirstName(),member2.getLastName(),member2.getAcademicTitle().getTitle(),member2.getEducationTitle().getTitle(),member2.getScientificField().getScfField(),member2.getDepartment().getName());

        when(scientificFieldRepository.findById(scientificField.getId())).thenReturn(Optional.of(scientificField));
        when(memberRepository.findByScientificFieldId(scientificField.getId())).thenReturn(Arrays.asList(member1,member2));
        when(memberConverter.toDto(member1)).thenReturn(memberDto1);
        when(memberConverter.toDto(member2)).thenReturn(memberDto2);

        List<MemberDto> returnedList = memberService.getAllByScientificField(scientificField.getId());
        verify(memberRepository,times(1)).findByScientificFieldId(scientificField.getId());
        verify(memberConverter,times(1)).toDto(member1);
        verify(memberConverter,times(1)).toDto(member2);

        assertEquals(2,returnedList.size());
        assertEquals(member1.getId(),returnedList.get(0).getId());
        assertEquals(member2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void getAllByScientificFieldNotExistingTest(){
        when(scientificFieldRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.getAllByScientificField(1L));
    }
    @Test
    public void getAllByScientificFieldEmptyListTest(){
        ScientificField scientificField = new ScientificField(1L,"Scientific field");
        when(scientificFieldRepository.findById(1L)).thenReturn(Optional.of(scientificField));

        when(memberRepository.findByScientificFieldId(scientificField.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> memberService.getAllByScientificField(1L));
    }
    @Test
    public void findByIdTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());

        when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));
        when(memberConverter.toDto(member1)).thenReturn(memberDto1);

        MemberDto returned = memberService.findById(member1.getId());

        verify(memberRepository,times(1)).findById(member1.getId());
        verify(memberConverter,times(1)).toDto(member1);

        assertEquals(memberDto1,returned);
    }
    @Test
    public void findByIdFailureTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->memberService.findById(1L));
    }
    @Test
    public void getHistoryTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto1 = new MemberDto(member1.getId(),member1.getFirstName(),member1.getLastName(),member1.getAcademicTitle().getTitle(),member1.getEducationTitle().getTitle(),member1.getScientificField().getScfField(),member1.getDepartment().getName());

        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory(26L,member1, LocalDate.now(),null,academicTitle,scientificField);
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(26L,memberDto1,academicTitleHistory.getStartDate(),null,academicTitleHistory.getAcademicTitle().getTitle(),academicTitleHistory.getScientificField().getScfField());

        when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));
        when(academicTitleHistoryRepository.findByMemberId(member1.getId())).thenReturn(Arrays.asList(academicTitleHistory));
        when(academicTitleHistoryConverter.toDto(academicTitleHistory)).thenReturn(academicTitleHistoryDto);

        List<AcademicTitleHistoryDto> returnedList = memberService.getHistory(member1.getId());

        verify(memberRepository,times(1)).findById(member1.getId());
        verify(academicTitleHistoryRepository,times(1)).findByMemberId(member1.getId());
        verify(academicTitleHistoryConverter,times(1)).toDto(academicTitleHistory);

        assertEquals(1,returnedList.size());
        assertEquals(academicTitleHistoryDto,returnedList.get(0));
    }
    @Test
    public void getHistoryNotExistingMemberTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.getHistory(1L));
    }
    @Test
    public void getHistoryEmptyList(){
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member1 = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);

        when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));
        when(academicTitleHistoryRepository.findByMemberId(member1.getId())).thenReturn(Collections.emptyList());

        assertThrows(NullPointerException.class,()-> memberService.getHistory(member1.getId()));
    }
    @Test
    public void deleteTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleHistoryRepository.findByMemberId(member.getId())).thenReturn(Collections.emptyList());
        when(managementRepository.findByMemberId(member.getId())).thenReturn(Collections.emptyList());

        memberService.delete(member.getId());
        verify(memberRepository,times(1)).delete(member);
    }
    @Test
    public void deleteNotFoundTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.delete(1L));
    }
    @Test
    public void deleteFailureAcademicTitleHistoryTest(){
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory(26L,member, LocalDate.now(),null,academicTitle,scientificField);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleHistoryRepository.findByMemberId(member.getId())).thenReturn(Arrays.asList(academicTitleHistory));

        assertThrows(Exception.class,()-> memberService.delete(1L));
    }
    @Test
    public void deleteFailureManagementTest(){
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        Management management = new Management(26L, department,member,"secretary",LocalDate.of(2023,3,3),LocalDate.of(23023,5,5));

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleHistoryRepository.findByMemberId(member.getId())).thenReturn(Collections.emptyList());
        when(managementRepository.findByMemberId(member.getId())).thenReturn(Arrays.asList(management));

        assertThrows(Exception.class,()-> memberService.delete(1L));
    }
    @Test
    public void saveTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),academicTitle.getTitle())).thenReturn(new AcademicTitleHistoryDto(30L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));

        memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void saveNotExistingDepartmentTest(){
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);

        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()-> memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName()));
    }
    @Test
    public void saveNewAcademicTitleTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.empty());
        when(academicTitleRepository.findMaxId()).thenReturn(1L);
        when(academicTitleRepository.save(any())).thenAnswer(invocation -> {
            AcademicTitle academicTitleObj = invocation.getArgument(0);
            academicTitleObj.setId(1L);
            return academicTitleObj;
        });
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),academicTitle.getTitle())).thenReturn(new AcademicTitleHistoryDto(30L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));

        memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void saveNewEducationTitleTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.empty());
        when(educationTitleRepository.findMaxId()).thenReturn(1L);
        when(educationTitleRepository.save(any())).thenAnswer(invocation -> {
            EducationTitle educationTitleObj = invocation.getArgument(0);
            educationTitleObj.setId(1L);
            return educationTitleObj;
        });
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),academicTitle.getTitle())).thenReturn(new AcademicTitleHistoryDto(30L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));

        memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void saveNewScientificFieldTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.empty());
        when(scientificFieldRepository.findMaxId()).thenReturn(1L);
        when(scientificFieldRepository.save(any())).thenAnswer(invocation -> {
            ScientificField scientificFieldObj = invocation.getArgument(0);
            scientificFieldObj.setId(1L);
            return scientificFieldObj;
        });
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),academicTitle.getTitle())).thenReturn(new AcademicTitleHistoryDto(30L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));

        memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void updateTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),member.getAcademicTitle().getTitle())).thenReturn(new AcademicTitleHistoryDto(1L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.update(memberDto);
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void updateNotExistingTest(){
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);

        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName()));
    }
    @Test
    public void updateNotExistingDepartmentTest()throws Exception{
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),member.getAcademicTitle().getTitle())).thenReturn(new AcademicTitleHistoryDto(1L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()-> memberService.save(member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName()));
    }
    @Test
    public void updateNewAcademicTitleTest() throws Exception{
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.empty());
        when(academicTitleRepository.findMaxId()).thenReturn(1L);
        when(academicTitleRepository.save(any())).thenAnswer(invocation -> {
            AcademicTitle academicTitleObj = invocation.getArgument(0);
            academicTitleObj.setId(1L);
            return academicTitleObj;
        });
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),member.getAcademicTitle().getTitle())).thenReturn(new AcademicTitleHistoryDto(1L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.update(memberDto);
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void updateNewEducationTitleTest() throws Exception{
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),member.getAcademicTitle().getTitle())).thenReturn(new AcademicTitleHistoryDto(1L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.empty());
        when(educationTitleRepository.findMaxId()).thenReturn(1L);
        when(educationTitleRepository.save(any())).thenAnswer(invocation -> {
            EducationTitle educationTitleObj = invocation.getArgument(0);
            educationTitleObj.setId(1L);
            return educationTitleObj;
        });
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.update(memberDto);
        verify(memberRepository,times(1)).save(any());
    }
    @Test
    public void updateNewScientificFieldTest() throws Exception{
        AcademicTitle academicTitle = new AcademicTitle(26L,"AT26");
        EducationTitle educationTitle = new EducationTitle(26L,"ET26");
        ScientificField scientificField = new ScientificField(26L,"ScF26");
        Department department = new Department(26L,"Department26");

        Member member = new Member(20L,"Anna","Lucas",academicTitle,educationTitle,scientificField,department);
        MemberDto memberDto = new MemberDto(member.getId(),member.getFirstName(),member.getLastName(),member.getAcademicTitle().getTitle(),member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));
        when(academicTitleHistoryService.save(member.getId(),LocalDate.now(),member.getAcademicTitle().getTitle())).thenReturn(new AcademicTitleHistoryDto(1L,memberDto,LocalDate.now(),null,academicTitle.getTitle(),scientificField.getScfField()));
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.empty());
        when(scientificFieldRepository.findMaxId()).thenReturn(1L);
        when(scientificFieldRepository.save(any())).thenAnswer(invocation -> {
            ScientificField scientificFieldObj = invocation.getArgument(0);
            scientificFieldObj.setId(1L);
            return scientificFieldObj;
        });
        when(departmentRepository.findByNameIgnoreCase(department.getName())).thenReturn(Optional.of(department));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.update(memberDto);
        verify(memberRepository,times(1)).save(any());
    }
}
