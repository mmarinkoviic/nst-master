package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.ScientificFieldConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.ScientificFieldDto;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
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
import static org.mockito.Mockito.*;

public class ScientificFieldServiceTests {
    @InjectMocks
    private ScientificFieldServiceImpl scientificFieldService;
    @Mock
    private ScientificFieldRepository scientificFieldRepository;
    @Mock
    private ScientificFieldConverter scientificFieldConverter;
    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTest(){
        ScientificField scientificField1 = new ScientificField(20L,"Scientific Field 21");
        ScientificField scientificField2 = new ScientificField(21L,"Scientific Field 21");

        when(scientificFieldRepository.findAll()).thenReturn(Arrays.asList(scientificField1,scientificField2));
        when(scientificFieldConverter.toDto(scientificField1)).thenReturn(new ScientificFieldDto(scientificField1.getId(),scientificField1.getScfField()));
        when(scientificFieldConverter.toDto(scientificField2)).thenReturn(new ScientificFieldDto(scientificField2.getId(),scientificField2.getScfField()));

        List<ScientificFieldDto> returnedList = scientificFieldService.getAll();

        verify(scientificFieldRepository,times(1)).findAll();
        verify(scientificFieldConverter,times(1)).toDto(scientificField1);
        verify(scientificFieldConverter,times(1)).toDto(scientificField2);

        assertEquals(2,returnedList.size());
        assertEquals(scientificField1.getScfField(),returnedList.get(0).getScfField());
        assertEquals(scientificField2.getScfField(),returnedList.get(1).getScfField());
    }
    @Test
    public void findByIdTest() throws Exception {
        ScientificField scientificField = new ScientificField(22L,"Scientific Field 22");

        when(scientificFieldRepository.findById(scientificField.getId())).thenReturn(Optional.of(scientificField));
        when(scientificFieldConverter.toDto(scientificField)).thenReturn(new ScientificFieldDto(scientificField.getId(), scientificField.getScfField()));

        ScientificFieldDto scientificFieldDto = scientificFieldService.findById(scientificField.getId());

        verify(scientificFieldRepository,times(1)).findById(scientificField.getId());
        verify(scientificFieldConverter,times(1)).toDto(scientificField);

        assertEquals(scientificField.getScfField(),scientificFieldDto.getScfField());
        assertEquals(scientificField.getId(), scientificFieldDto.getId());
    }
    @Test
    public void findByIdFailureTest(){
        when(scientificFieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->scientificFieldService.findById(1L));
    }
    @Test
    public void deleteTest() throws Exception {
        ScientificField scientificField = new ScientificField(23L,"Scientific Field 23");
        when(scientificFieldRepository.findById(scientificField.getId())).thenReturn(Optional.of(scientificField));
        when(memberRepository.findByEducationTitleId(scientificField.getId())).thenReturn(Collections.emptyList());

        scientificFieldService.delete(scientificField.getId());
        verify(scientificFieldRepository,times(1)).delete(scientificField);
    }
    @Test
    public void deleteNotFoundTest(){
        when(scientificFieldRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->scientificFieldService.delete(1L));
    }
    @Test
    public void deleteFoundMemberListTest(){
        ScientificField scientificField = new ScientificField(23L,"Scientific Field 23");
        when(scientificFieldRepository.findById(scientificField.getId())).thenReturn(Optional.of(scientificField));
        Member member = new Member(1L,"Katy","Perry",new AcademicTitle(1L,"Academic Title"),new EducationTitle(1L,"Education Title"),scientificField,new Department(1L,"Department"));
        when(memberRepository.findByScientificFieldId(scientificField.getId())).thenReturn(Arrays.asList(member));

        assertThrows(Exception.class,()->scientificFieldService.delete(scientificField.getId()));
    }
    @Test
    public void updateTest() throws Exception{
        ScientificField scientificField = new ScientificField(24L,"Scientific Field");
        String newTitle = "Scientific Field 24";

        when(scientificFieldRepository.findByScfFieldIgnoreCase(newTitle)).thenReturn(Optional.empty());
        when(scientificFieldRepository.findById(scientificField.getId())).thenReturn(Optional.of(scientificField));

        scientificFieldService.update(scientificField.getId(),newTitle);
        verify(scientificFieldRepository,times(1)).save(scientificField);
    }
    @Test
    public void updateExistingNameTest(){
        ScientificField scientificField = new ScientificField(24L,"Scientific Field");
        when(scientificFieldRepository.findByScfFieldIgnoreCase(scientificField.getScfField())).thenReturn(Optional.of(scientificField));

        assertThrows(EntityExistsException.class,()->scientificFieldService.update(scientificField.getId(),scientificField.getScfField()));
    }
    @Test
    public void updateNotExistingTest(){
        when(scientificFieldRepository.findByScfFieldIgnoreCase("New")).thenReturn(Optional.empty());
        when(scientificFieldRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->scientificFieldService.update(2L,"New"));
    }
    @Test
    public void saveTest() throws Exception{
        String newName = "Scientific Field New";
        when(scientificFieldRepository.findByScfFieldIgnoreCase(newName)).thenReturn(Optional.empty());
        when(scientificFieldRepository.findMaxId()).thenReturn(154L);

        ScientificField scientificField = new ScientificField(154L, newName);
        when(scientificFieldRepository.save(any())).thenReturn(scientificField);
        ScientificFieldDto expected = new ScientificFieldDto(154L, newName);
        when(scientificFieldConverter.toDto(any())).thenReturn(expected);

        ScientificFieldDto returned = scientificFieldService.save(newName);
        assertEquals(expected,returned);
    }
    @Test
    public void saveExistingNameTest(){
        String newName = "Education Title Existing";
        ScientificField scientificField = new ScientificField(28L,newName);
        when(scientificFieldRepository.findByScfFieldIgnoreCase(newName)).thenReturn(Optional.of(scientificField));

        assertThrows(EntityExistsException.class,()->scientificFieldService.save(newName));
    }
}
