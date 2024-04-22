package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.repository.AcademicTitleHistoryRepository;
import nst.springboot.restexample01.repository.AcademicTitleRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
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

public class AcademicTitleHistoryServiceTests {
    @InjectMocks
    private AcademicTitleHistoryServiceImpl academicTitleHistoryService;
    @Mock
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    @Mock
    private AcademicTitleHistoryConverter academicTitleHistoryConverter;
    @Mock
    private ScientificFieldRepository scientificFieldRepository;
    @Mock
    private AcademicTitleRepository academicTitleRepository;
    @Mock
    private MemberRepository memberRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        AcademicTitle academicTitle2 = new AcademicTitle(26L,"AT2");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(1L,"Marry","Alice", academicTitle1.getTitle(), educationTitle.getTitle(), scientificField.getScfField(), department.getName());
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),LocalDate.of(2024,3,3),academicTitle1,scientificField);
        AcademicTitleHistory academicTitleHistory2 = new AcademicTitleHistory(21L, member, LocalDate.of(2024,3,3),null,academicTitle2,scientificField);

        when(academicTitleHistoryRepository.findAll()).thenReturn(Arrays.asList(academicTitleHistory1,academicTitleHistory2));
        when(academicTitleHistoryConverter.toDto(academicTitleHistory1)).thenReturn(new AcademicTitleHistoryDto(academicTitleHistory1.getId(),memberDto,academicTitleHistory1.getStartDate(), academicTitleHistory1.getEndDate(),academicTitleHistory1.getAcademicTitle().getTitle(),academicTitleHistory1.getScientificField().getScfField()));
        when(academicTitleHistoryConverter.toDto(academicTitleHistory2)).thenReturn(new AcademicTitleHistoryDto(academicTitleHistory2.getId(),memberDto,academicTitleHistory2.getStartDate(), academicTitleHistory2.getEndDate(),academicTitleHistory2.getAcademicTitle().getTitle(),academicTitleHistory2.getScientificField().getScfField()));

        List<AcademicTitleHistoryDto> returnedList = academicTitleHistoryService.getAll();

        verify(academicTitleHistoryRepository,times(1)).findAll();
        verify(academicTitleHistoryConverter,times(1)).toDto(academicTitleHistory1);
        verify(academicTitleHistoryConverter,times(1)).toDto(academicTitleHistory2);

        assertEquals(2,returnedList.size());
        assertEquals(academicTitleHistory1.getId(),returnedList.get(0).getId());
        assertEquals(academicTitleHistory2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void findByIdTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(1L,"Marry","Alice", academicTitle1.getTitle(), educationTitle.getTitle(), scientificField.getScfField(), department.getName());
        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),LocalDate.of(2024,3,3),academicTitle1,scientificField);

        when(academicTitleHistoryRepository.findById(academicTitleHistory.getId())).thenReturn(Optional.of(academicTitleHistory));
        when(academicTitleHistoryConverter.toDto(academicTitleHistory)).thenReturn(new AcademicTitleHistoryDto(academicTitleHistory.getId(),memberDto,academicTitleHistory.getStartDate(), academicTitleHistory.getEndDate(),academicTitleHistory.getAcademicTitle().getTitle(),academicTitleHistory.getScientificField().getScfField()));

        AcademicTitleHistoryDto academicTitleHistoryDto = academicTitleHistoryService.findById(academicTitleHistory.getId());

        verify(academicTitleHistoryRepository,times(1)).findById(academicTitleHistory.getId());
        verify(academicTitleHistoryConverter,times(1)).toDto(academicTitleHistory);

        assertEquals(academicTitleHistory.getId(),academicTitleHistoryDto.getId());
    }
    @Test
    public void findByIdFailureTest(){
        when(academicTitleHistoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.findById(1L));
    }
    @Test
    public void getAllByMemberTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        AcademicTitle academicTitle2 = new AcademicTitle(26L,"AT2");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(1L,"Marry","Alice", academicTitle1.getTitle(), educationTitle.getTitle(), scientificField.getScfField(), department.getName());
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),LocalDate.of(2024,3,3),academicTitle1,scientificField);
        AcademicTitleHistory academicTitleHistory2 = new AcademicTitleHistory(21L, member, LocalDate.of(2024,3,3),null,academicTitle2,scientificField);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        when(academicTitleHistoryRepository.findByMemberId(member.getId())).thenReturn(Arrays.asList(academicTitleHistory1,academicTitleHistory2));
        when(academicTitleHistoryConverter.toDto(academicTitleHistory1)).thenReturn(new AcademicTitleHistoryDto(academicTitleHistory1.getId(),memberDto,academicTitleHistory1.getStartDate(), academicTitleHistory1.getEndDate(),academicTitleHistory1.getAcademicTitle().getTitle(),academicTitleHistory1.getScientificField().getScfField()));
        when(academicTitleHistoryConverter.toDto(academicTitleHistory2)).thenReturn(new AcademicTitleHistoryDto(academicTitleHistory2.getId(),memberDto,academicTitleHistory2.getStartDate(), academicTitleHistory2.getEndDate(),academicTitleHistory2.getAcademicTitle().getTitle(),academicTitleHistory2.getScientificField().getScfField()));

        List<AcademicTitleHistoryDto> returnedList = academicTitleHistoryService.getAllByMember(member.getId());

        verify(academicTitleHistoryRepository,times(1)).findByMemberId(member.getId());
        verify(academicTitleHistoryConverter,times(1)).toDto(academicTitleHistory1);
        verify(academicTitleHistoryConverter,times(1)).toDto(academicTitleHistory2);

        assertEquals(2,returnedList.size());
        assertEquals(academicTitleHistory1.getId(),returnedList.get(0).getId());
        assertEquals(academicTitleHistory2.getId(),returnedList.get(1).getId());
    }
    @Test
    public void getAllByMemberNotExistingTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.getAllByMember(1L));
    }
    @Test
    public void getAllByMemberEmptyListTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        when(academicTitleHistoryRepository.findByMemberId(member.getId())).thenReturn(Collections.emptyList());
        assertThrows(NullPointerException.class,()-> academicTitleHistoryService.getAllByMember(1L));
    }
    @Test
    public void deleteTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),LocalDate.of(2024,3,3),academicTitle1,scientificField);

        when(academicTitleHistoryRepository.findById(academicTitleHistory1.getId())).thenReturn(Optional.of(academicTitleHistory1));

        academicTitleHistoryService.delete(academicTitleHistory1.getId());
        verify(academicTitleHistoryRepository,times(1)).delete(academicTitleHistory1);
    }
    @Test
    public void deleteNotFoundTest(){
        when(academicTitleHistoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.delete(1L));
    }
    @Test
    public void deleteNotApprovedTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),null,academicTitle1,scientificField);

        when(academicTitleHistoryRepository.findById(academicTitleHistory1.getId())).thenReturn(Optional.of(academicTitleHistory1));

        assertThrows(Exception.class,()-> academicTitleHistoryService.delete(academicTitleHistory1.getId()));
    }
    @Test
    public void updateTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),null,academicTitle1,scientificField);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleHistoryRepository.findByEndDateIsNullAndMemberId(member.getId())).thenReturn(Optional.of(academicTitleHistory1));

        academicTitleHistoryService.update(member.getId(), LocalDate.now());
        verify(academicTitleHistoryRepository,times(1)).save(any());
    }
    @Test
    public void updateNotExistingTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.update(1L,LocalDate.now()));
    }
    @Test
    public void updateFailureTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(1L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),null,academicTitle1,scientificField);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleHistoryRepository.findByEndDateIsNullAndMemberId(member.getId())).thenReturn(Optional.empty());

        academicTitleHistoryService.update(member.getId(), LocalDate.now());
        verify(academicTitleHistoryRepository,times(0)).save(any());
    }
    @Test
    public void saveTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(29L,"Marry","Alice", academicTitle1.getTitle(), educationTitle.getTitle(), scientificField.getScfField(), department.getName());
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,3,3),null,academicTitle1,scientificField);
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(academicTitleHistory1.getId(),memberDto,academicTitleHistory1.getStartDate(), academicTitleHistory1.getEndDate(),academicTitleHistory1.getAcademicTitle().getTitle(),academicTitleHistory1.getScientificField().getScfField());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.of(academicTitle1));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(member.getId(),academicTitle1.getTitle())).thenReturn(Optional.empty());
        when(academicTitleHistoryRepository.save(any())).thenReturn(academicTitleHistory1);
        when(academicTitleHistoryConverter.toDto(academicTitleHistory1)).thenReturn(academicTitleHistoryDto);

        AcademicTitleHistoryDto returned = academicTitleHistoryService.save(member.getId(),LocalDate.of(2024,3,3),academicTitle1.getTitle());

        verify(memberRepository,times(1)).save(any());
        verify(academicTitleHistoryRepository,times(1)).save(any());

        assertEquals(academicTitleHistoryDto.getId(),returned.getId());
    }
    @Test
    public void saveNotExistingMemberTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.save(1L,LocalDate.now(),"Academic Title"));
    }
    @Test
    public void saveFutureDateTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        assertThrows(IllegalArgumentException.class,()-> academicTitleHistoryService.save(member.getId(), LocalDate.of(2025,5,5),"Academic Title"));
    }
    @Test
    public void saveNotExistingAcademicTitleTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.save(member.getId(), LocalDate.now(),"Academic Title"));
    }
    @Test
    public void saveExistingTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2024,3,3),LocalDate.of(2024,5,5),academicTitle1,scientificField);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.of(academicTitle1));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(member.getId(),academicTitle1.getTitle())).thenReturn(Optional.of(academicTitleHistory1));
        assertThrows(EntityExistsException.class,()-> academicTitleHistoryService.save(member.getId(), LocalDate.now(),academicTitle1.getTitle()));
    }
    @Test
    public void savePreviousTest() throws Exception {
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        MemberDto memberDto = new MemberDto(29L,"Marry","Alice", academicTitle1.getTitle(), educationTitle.getTitle(), scientificField.getScfField(), department.getName());
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2023,3,3),LocalDate.of(2023,5,5),academicTitle1,scientificField);
        AcademicTitleHistory current = new AcademicTitleHistory(20L, member, LocalDate.of(2024,1,1),null,academicTitle1,scientificField);
        AcademicTitleHistoryDto academicTitleHistoryDto = new AcademicTitleHistoryDto(academicTitleHistory1.getId(),memberDto,academicTitleHistory1.getStartDate(), academicTitleHistory1.getEndDate(),academicTitleHistory1.getAcademicTitle().getTitle(),academicTitleHistory1.getScientificField().getScfField());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.of(academicTitle1));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(member.getId(),academicTitle1.getTitle())).thenReturn(Optional.empty());
        when(academicTitleHistoryRepository.findByEndDateIsNullAndMemberId(member.getId())).thenReturn(Optional.of(current));
        when(academicTitleHistoryRepository.save(any())).thenReturn(academicTitleHistory1);
        when(academicTitleHistoryConverter.toDto(academicTitleHistory1)).thenReturn(academicTitleHistoryDto);

        AcademicTitleHistoryDto returned = academicTitleHistoryService.savePrevious(member.getId(),LocalDate.of(2023,3,3), LocalDate.of(2023,5,5), academicTitle1.getTitle());

        verify(academicTitleHistoryRepository,times(1)).save(any());

        assertEquals(academicTitleHistoryDto.getId(),returned.getId());
    }
    @Test
    public void savePreviousNotExistingMemberTest(){
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.savePrevious(1L,LocalDate.of(2023,3,3),LocalDate.of(2023,5,5),"Academic Title"));
    }
    @Test
    public void savePreviousIllegalDateTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        assertThrows(IllegalArgumentException.class,()-> academicTitleHistoryService.savePrevious(member.getId(), LocalDate.of(2023,5,5),LocalDate.of(2023,3,3),"Academic Title"));
    }
    @Test
    public void savePreviousNotExistingAcademicTitleTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> academicTitleHistoryService.savePrevious(member.getId(), LocalDate.of(2023,3,3),LocalDate.of(2023,5,5),"Academic Title"));
    }
    @Test
    public void savePreviousExistingTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2023,3,3),LocalDate.of(2023,5,5),academicTitle1,scientificField);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.of(academicTitle1));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(member.getId(),academicTitle1.getTitle())).thenReturn(Optional.of(academicTitleHistory1));
        assertThrows(EntityExistsException.class,()-> academicTitleHistoryService.savePrevious(member.getId(), LocalDate.of(2023,3,3),LocalDate.of(2023,5,5),academicTitle1.getTitle()));
    }
    @Test
    public void savePreviousIllegalDatesTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(25L,"AT1");
        EducationTitle educationTitle = new EducationTitle(25L,"ET1");
        ScientificField scientificField = new ScientificField(25L,"ScF1");
        Department department = new Department(25L, "Department");
        Member member = new Member(29L,"Marry","Alice", academicTitle1, educationTitle, scientificField, department);
        AcademicTitleHistory academicTitleHistory1 = new AcademicTitleHistory(20L, member, LocalDate.of(2023,3,3),LocalDate.of(2023,5,5),academicTitle1,scientificField);
        AcademicTitleHistory current = new AcademicTitleHistory(20L, member, LocalDate.of(2023,5,1),null,academicTitle1,scientificField);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle1.getTitle())).thenReturn(Optional.of(academicTitle1));
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));
        when(academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(member.getId(),academicTitle1.getTitle())).thenReturn(Optional.of(academicTitleHistory1));
        when(academicTitleHistoryRepository.findByEndDateIsNullAndMemberId(member.getId())).thenReturn(Optional.of(current));


        assertThrows(EntityExistsException.class,()-> academicTitleHistoryService.savePrevious(member.getId(), LocalDate.of(2023,3,3),LocalDate.of(2023,5,3),academicTitle1.getTitle()));
    }
}
