package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.EducationTitleConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.EducationTitleDto;
import nst.springboot.restexample01.repository.EducationTitleRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EducationTitleServiceTests {

    @InjectMocks
    private EducationTitleServiceImpl educationTitleService;
    @Mock
    private EducationTitleRepository educationTitleRepository;
    @Mock
    private EducationTitleConverter educationTitleConverter;
    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTest(){
        EducationTitle educationTitle1 = new EducationTitle(20L,"Education Title 20");
        EducationTitle educationTitle2 = new EducationTitle(21L,"Education Title 21");

        when(educationTitleRepository.findAll()).thenReturn(Arrays.asList(educationTitle1,educationTitle2));
        when(educationTitleConverter.toDto(educationTitle1)).thenReturn(new EducationTitleDto(educationTitle1.getId(),educationTitle1.getTitle()));
        when(educationTitleConverter.toDto(educationTitle2)).thenReturn(new EducationTitleDto(educationTitle2.getId(),educationTitle2.getTitle()));

        List<EducationTitleDto> returnedList = educationTitleService.getAll();

        verify(educationTitleRepository,times(1)).findAll();
        verify(educationTitleConverter,times(1)).toDto(educationTitle1);
        verify(educationTitleConverter,times(1)).toDto(educationTitle2);

        assertEquals(2,returnedList.size());
        assertEquals(educationTitle1.getTitle(),returnedList.get(0).getTitle());
        assertEquals(educationTitle2.getTitle(),returnedList.get(1).getTitle());
    }
    @Test
    public void findByIdTest() throws Exception {
        EducationTitle educationTitle = new EducationTitle(22L,"Education Title 22");

        when(educationTitleRepository.findById(educationTitle.getId())).thenReturn(Optional.of(educationTitle));
        when(educationTitleConverter.toDto(educationTitle)).thenReturn(new EducationTitleDto(educationTitle.getId(),educationTitle.getTitle()));

        EducationTitleDto educationTitleDto = educationTitleService.findById(educationTitle.getId());

        verify(educationTitleRepository,times(1)).findById(educationTitle.getId());
        verify(educationTitleConverter,times(1)).toDto(educationTitle);

        assertEquals(educationTitle.getTitle(),educationTitleDto.getTitle());
        assertEquals(educationTitle.getId(),educationTitleDto.getId());
    }
    @Test
    public void findByIdFailureTest(){
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->educationTitleService.findById(1L));
    }
    @Test
    public void deleteTest() throws Exception {
        EducationTitle educationTitle = new EducationTitle(23L,"Education Title 23");
        when(educationTitleRepository.findById(educationTitle.getId())).thenReturn(Optional.of(educationTitle));
        when(memberRepository.findByEducationTitleId(educationTitle.getId())).thenReturn(Collections.emptyList());

        educationTitleService.delete(educationTitle.getId());
        verify(educationTitleRepository,times(1)).delete(educationTitle);
    }
    @Test
    public void deleteNotFoundTest(){
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->educationTitleService.delete(1L));
    }
    @Test
    public void deleteFoundMemberListTest(){
        EducationTitle educationTitle = new EducationTitle(23L,"Education Title 23");
        when(educationTitleRepository.findById(educationTitle.getId())).thenReturn(Optional.of(educationTitle));
        Member member = new Member(1L,"Katy","Perry",new AcademicTitle(1L,"Academic Title"),educationTitle,new ScientificField(1L,"Scientific Field"),new Department(1L,"Department"));
        when(memberRepository.findByEducationTitleId(educationTitle.getId())).thenReturn(Arrays.asList(member));

        assertThrows(Exception.class,()->educationTitleService.delete(educationTitle.getId()));
    }
    @Test
    public void updateTest() throws Exception{
        EducationTitle educationTitle = new EducationTitle(24L,"Education Title");
        String newTitle = "Education Title 24";

        when(educationTitleRepository.findByTitleIgnoreCase(newTitle)).thenReturn(Optional.empty());
        when(educationTitleRepository.findById(educationTitle.getId())).thenReturn(Optional.of(educationTitle));

        educationTitleService.update(educationTitle.getId(),newTitle);
        verify(educationTitleRepository,times(1)).save(educationTitle);
    }
    @Test
    public void updateExistingNameTest(){
        EducationTitle educationTitle = new EducationTitle(24L,"Education Title");
        when(educationTitleRepository.findByTitleIgnoreCase(educationTitle.getTitle())).thenReturn(Optional.of(educationTitle));

        assertThrows(EntityExistsException.class,()->educationTitleService.update(educationTitle.getId(),educationTitle.getTitle()));
    }
    @Test
    public void updateNotExistingTest(){
        when(educationTitleRepository.findByTitleIgnoreCase("New")).thenReturn(Optional.empty());
        when(educationTitleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->educationTitleService.update(2L,"New"));
    }
    @Test
    public void saveTest() throws Exception{
        String newName = "Education Title New";
        when(educationTitleRepository.findByTitleIgnoreCase(newName)).thenReturn(Optional.empty());
        when(educationTitleRepository.findMaxId()).thenReturn(154L);

        EducationTitle educationTitle = new EducationTitle(154L,newName);
        when(educationTitleRepository.save(any())).thenReturn(educationTitle);
        EducationTitleDto expected = new EducationTitleDto(154L,newName);
        when(educationTitleConverter.toDto(any())).thenReturn(expected);

        EducationTitleDto returned = educationTitleService.save(newName);
        assertEquals(expected,returned);
    }
    @Test
    public void saveExistingNameTest(){
        String newName = "Education Title Existing";
        EducationTitle educationTitle = new EducationTitle(28L,newName);
        when(educationTitleRepository.findByTitleIgnoreCase(newName)).thenReturn(Optional.of(educationTitle));

        assertThrows(EntityExistsException.class,()->educationTitleService.save(newName));
    }
}
