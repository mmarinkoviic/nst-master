package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.AcademicTitleConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.AcademicTitleDto;
import nst.springboot.restexample01.dto.EducationTitleDto;
import nst.springboot.restexample01.repository.AcademicTitleRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class AcademicTitleServiceTests {
    @InjectMocks
    private AcademicTitleServiceImpl academicTitleService;
    @Mock
    private AcademicTitleRepository academicTitleRepository;
    @Mock
    private AcademicTitleConverter academicTitleConverter;
    @Mock
    private MemberRepository memberRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getAllTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(20L,"Academic Title 20");
        AcademicTitle academicTitle2 = new AcademicTitle(21L,"Academic Title 21");

        when(academicTitleRepository.findAll()).thenReturn(Arrays.asList(academicTitle1, academicTitle2));
        when(academicTitleConverter.toDto(academicTitle1)).thenReturn(new AcademicTitleDto(academicTitle1.getId(), academicTitle1.getTitle()));
        when(academicTitleConverter.toDto(academicTitle2)).thenReturn(new AcademicTitleDto(academicTitle2.getId(), academicTitle2.getTitle()));

        List<AcademicTitleDto> returnedList = academicTitleService.getAll();

        verify(academicTitleRepository,times(1)).findAll();
        verify(academicTitleConverter,times(1)).toDto(academicTitle1);
        verify(academicTitleConverter,times(1)).toDto(academicTitle2);

        assertEquals(2,returnedList.size());
        assertEquals(academicTitle1.getTitle(),returnedList.get(0).getTitle());
        assertEquals(academicTitle2.getTitle(),returnedList.get(1).getTitle());
    }
    @Test
    public void findByIdTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(22L,"Academic Title 22");

        when(academicTitleRepository.findById(academicTitle.getId())).thenReturn(Optional.of(academicTitle));
        when(academicTitleConverter.toDto(academicTitle)).thenReturn(new AcademicTitleDto(academicTitle.getId(), academicTitle.getTitle()));

        AcademicTitleDto academicTitleDto = academicTitleService.findById(academicTitle.getId());

        verify(academicTitleRepository,times(1)).findById(academicTitle.getId());
        verify(academicTitleConverter,times(1)).toDto(academicTitle);

        assertEquals(academicTitle.getTitle(),academicTitleDto.getTitle());
        assertEquals(academicTitle.getId(),academicTitleDto.getId());
    }
    @Test
    public void findByIdFailureTest(){
        when(academicTitleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->academicTitleService.findById(1L));
    }
    @Test
    public void deleteTest() throws Exception {
        AcademicTitle academicTitle = new AcademicTitle(23L,"Academic Title 23");
        when(academicTitleRepository.findById(academicTitle.getId())).thenReturn(Optional.of(academicTitle));
        when(memberRepository.findByAcademicTitleId(academicTitle.getId())).thenReturn(Collections.emptyList());

        academicTitleService.delete(academicTitle.getId());
        verify(academicTitleRepository,times(1)).delete(academicTitle);
    }
    @Test
    public void deleteNotFoundTest(){
        when(academicTitleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->academicTitleService.delete(1L));
    }
    @Test
    public void deleteFoundMemberListTest(){
        AcademicTitle academicTitle = new AcademicTitle(23L,"Academic Title 23");
        when(academicTitleRepository.findById(academicTitle.getId())).thenReturn(Optional.of(academicTitle));
        Member member = new Member(1L,"Katy","Perry",academicTitle,new EducationTitle(1L,"Education Title"),new ScientificField(1L,"Scientific Field"),new Department(1L,"Department"));
        when(memberRepository.findByAcademicTitleId(academicTitle.getId())).thenReturn(Arrays.asList(member));

        assertThrows(Exception.class,()->academicTitleService.delete(academicTitle.getId()));
    }
    @Test
    public void updateTest() throws Exception{
        AcademicTitle academicTitle = new AcademicTitle(24L,"Academic Title");
        String newTitle = "Academic Title 24";

        when(academicTitleRepository.findByTitleIgnoreCase(newTitle)).thenReturn(Optional.empty());
        when(academicTitleRepository.findById(academicTitle.getId())).thenReturn(Optional.of(academicTitle));

        academicTitleService.update(academicTitle.getId(),newTitle);
        verify(academicTitleRepository,times(1)).save(academicTitle);
    }
    @Test
    public void updateExistingNameTest(){
        AcademicTitle academicTitle = new AcademicTitle(24L,"Academic Title");
        when(academicTitleRepository.findByTitleIgnoreCase(academicTitle.getTitle())).thenReturn(Optional.of(academicTitle));

        assertThrows(EntityExistsException.class,()-> academicTitleService.update(academicTitle.getId(), academicTitle.getTitle()));
    }
    @Test
    public void updateNotExistingTest(){
        when(academicTitleRepository.findByTitleIgnoreCase("New")).thenReturn(Optional.empty());
        when(academicTitleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->academicTitleService.update(2L,"New"));
    }
    @Test
    public void saveTest() throws Exception{
        String newName = "Academic Title New";
        when(academicTitleRepository.findByTitleIgnoreCase(newName)).thenReturn(Optional.empty());
        when(academicTitleRepository.findMaxId()).thenReturn(154L);

        AcademicTitle academicTitle = new AcademicTitle(154L,newName);
        when(academicTitleRepository.save(any())).thenReturn(academicTitle);
        AcademicTitleDto expected = new AcademicTitleDto(154L,newName);
        when(academicTitleConverter.toDto(any())).thenReturn(expected);

        AcademicTitleDto returned = academicTitleService.save(newName);
        assertEquals(expected,returned);
    }
    @Test
    public void saveExistingNameTest(){
        String newName = "Academic Title Existing";
        AcademicTitle academicTitle = new AcademicTitle(28L,newName);
        when(academicTitleRepository.findByTitleIgnoreCase(newName)).thenReturn(Optional.of(academicTitle));

        assertThrows(EntityExistsException.class,()-> academicTitleService.save(newName));
    }
}
