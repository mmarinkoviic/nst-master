package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.converter.impl.SubjectConverter;
import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.domain.Subject;
import nst.springboot.restexample01.dto.SubjectDto;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.SubjectRepository;
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

public class SubjectServiceTests {
    @InjectMocks
    private SubjectServiceImpl subjectService;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SubjectConverter subjectConverter;
    @Mock
    private DepartmentRepository departmentRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getAllTest(){
        Subject subject1 = new Subject(20L,"Subject 20",6,new Department(1L,"Department"));
        Subject subject2 = new Subject(21L,"Subject 21",6,new Department(1L,"Department"));

        when(subjectRepository.findAll()).thenReturn(Arrays.asList(subject1, subject2));
        when(subjectConverter.toDto(subject1)).thenReturn(new SubjectDto(subject1.getId(),subject1.getName(),subject1.getEsbp(),subject1.getDepartment().getName()));
        when(subjectConverter.toDto(subject2)).thenReturn(new SubjectDto(subject2.getId(),subject2.getName(),subject2.getEsbp(),subject2.getDepartment().getName()));

        List<SubjectDto> returnedList = subjectService.getAll();

        verify(subjectRepository,times(1)).findAll();
        verify(subjectConverter,times(1)).toDto(subject1);
        verify(subjectConverter,times(1)).toDto(subject2);

        assertEquals(2,returnedList.size());
        assertEquals(subject1.getName(),returnedList.get(0).getName());
        assertEquals(subject1.getEsbp(),returnedList.get(0).getEsbp());
        assertEquals(subject1.getDepartment().getName(),returnedList.get(0).getDepartment());
        assertEquals(subject2.getName(),returnedList.get(1).getName());
        assertEquals(subject2.getEsbp(),returnedList.get(1).getEsbp());
        assertEquals(subject2.getDepartment().getName(),returnedList.get(1).getDepartment());
    }
    @Test
    public void findByIdTest() throws Exception {
        Subject subject = new Subject(22L,"Subject 22",6,new Department(1L,"Department"));

        when(subjectRepository.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectConverter.toDto(subject)).thenReturn(new SubjectDto(subject.getId(),subject.getName(),subject.getEsbp(),subject.getDepartment().getName()));

        SubjectDto subjectDto = subjectService.findById(subject.getId());

        verify(subjectRepository,times(1)).findById(subject.getId());
        verify(subjectConverter,times(1)).toDto(subject);

        assertEquals(subject.getName(),subjectDto.getName());
        assertEquals(subject.getId(), subjectDto.getId());
        assertEquals(subject.getEsbp(), subjectDto.getEsbp());
        assertEquals(subject.getDepartment().getName(), subjectDto.getDepartment());
    }
    @Test
    public void findByIdFailureTest(){
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> subjectService.findById(1L));
    }
    @Test
    public void deleteTest() throws Exception {
        Subject subject = new Subject(23L,"Subject 23",6,new Department(1L,"Department"));

        when(subjectRepository.findById(subject.getId())).thenReturn(Optional.of(subject));

        subjectService.delete(subject.getId());
        verify(subjectRepository,times(1)).delete(subject);
    }
    @Test
    public void deleteNotFoundTest(){
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> subjectService.delete(1L));
    }
    @Test
    public void updateTest() throws Exception{
        Department department = new Department(1L,"Department");
        Subject subject = new Subject(24L,"Subject",6,department);
        SubjectDto subjectDto = new SubjectDto(24L,"Subject 24",6,department.getName());

        when(subjectRepository.findByNameIgnoreCase(subjectDto.getName())).thenReturn(Optional.empty());
        when(subjectRepository.existsById(subject.getId())).thenReturn(true);
        when(departmentRepository.findByNameIgnoreCase(subjectDto.getDepartment())).thenReturn(Optional.of(department));
        when(subjectRepository.findById(subjectDto.getId())).thenReturn(Optional.of(subject));

        subjectService.update(subjectDto);
        verify(subjectRepository,times(1)).save(any());
    }
    @Test
    public void updateExistingNameTest(){
        Subject subject = new Subject(24L,"Subject",6,new Department(1L,"Department"));
        SubjectDto subjectDto = new SubjectDto(24L,"Subject",6,"Department");
        when(subjectRepository.findByNameIgnoreCase(subjectDto.getName())).thenReturn(Optional.of(subject));

        assertThrows(EntityExistsException.class,()-> subjectService.update(subjectDto));
    }
    @Test
    public void updateNotExistingTest(){
        SubjectDto subjectDto = new SubjectDto(2L,"Subject",6,"Department");
        when(subjectRepository.findByNameIgnoreCase("New")).thenReturn(Optional.empty());
        when(subjectRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()-> subjectService.update(subjectDto));
    }
    @Test
    public void updateNotExistingDepartmentTest(){
        SubjectDto subjectDto = new SubjectDto(2L,"Subject",6,"Department");
        Subject subject = new Subject(2L,"Subject",6,new Department(1L,"Department"));
        when(subjectRepository.findByNameIgnoreCase("New")).thenReturn(Optional.empty());
        when(subjectRepository.findById(2L)).thenReturn(Optional.of(subject));
        when(departmentRepository.findByNameIgnoreCase(subjectDto.getDepartment())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()-> subjectService.update(subjectDto));
    }
    @Test
    public void saveTest() throws Exception{
        String newName = "Subject New";
        String newDepartment = "Department";
        Department department = new Department(1L, newDepartment);

        when(subjectRepository.findByNameIgnoreCase(newName)).thenReturn(Optional.empty());
        when(departmentRepository.findByNameIgnoreCase(newDepartment)).thenReturn(Optional.of(department));
        when(subjectRepository.findMaxId()).thenReturn(154L);

        Subject subject = new Subject(154L,newName,6,department);
        when(subjectRepository.save(any())).thenReturn(subject);
        SubjectDto expected = new SubjectDto(154L,newName,6,newDepartment);
        when(subjectConverter.toDto(any())).thenReturn(expected);

        SubjectDto returned = subjectService.save(newName,6,newDepartment);
        assertEquals(expected,returned);
    }
    @Test
    public void saveExistingNameTest(){
        String newName = "Subject Existing";
        Department department = new Department(1L,"Department");
        Subject subject = new Subject(28L,newName,6,department);
        when(subjectRepository.findByNameIgnoreCase(newName)).thenReturn(Optional.of(subject));

        assertThrows(EntityExistsException.class,()->subjectService.save(newName,6,department.getName()));
    }
    @Test
    public void saveNotExistingDepartmentTest(){
        String newName = "Subject Existing";
        String department = "Department";

        when(subjectRepository.findByNameIgnoreCase(newName)).thenReturn(Optional.empty());
        when(departmentRepository.findByNameIgnoreCase(department)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,()->subjectService.save(newName,6,department));
    }
    @Test
    public void findByDepartmentTest() throws Exception {
        Department department = new Department(1L,"Department");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Subject subject1 = new Subject(30L, "Subject 1",6,department);
        Subject subject2 = new Subject(31L, "Subject 2",6,department);

        SubjectDto subjectDto1 = new SubjectDto(subject1.getId(),subject1.getName(),subject1.getEsbp(),subject1.getDepartment().getName());
        SubjectDto subjectDto2 = new SubjectDto(subject2.getId(),subject2.getName(),subject2.getEsbp(),subject2.getDepartment().getName());

        when(subjectRepository.findByDepartmentId(department.getId())).thenReturn(Arrays.asList(subject1,subject2));
        when(subjectConverter.toDto(subject1)).thenReturn(subjectDto1);
        when(subjectConverter.toDto(subject2)).thenReturn(subjectDto2);

        List<SubjectDto> returnedList = subjectService.findByDepartment(department.getId());

        verify(subjectRepository,times(1)).findByDepartmentId(department.getId());
        verify(subjectConverter,times(1)).toDto(subject1);
        verify(subjectConverter,times(1)).toDto(subject2);

        assertEquals(2,returnedList.size());
        assertEquals(subject1.getName(),returnedList.get(0).getName());
        assertEquals(subject1.getEsbp(),returnedList.get(0).getEsbp());
        assertEquals(subject1.getDepartment().getName(),returnedList.get(0).getDepartment());
        assertEquals(subject2.getName(),returnedList.get(1).getName());
        assertEquals(subject2.getEsbp(),returnedList.get(1).getEsbp());
        assertEquals(subject2.getDepartment().getName(),returnedList.get(1).getDepartment());
    }
    @Test
    public void findByDepartmentNotExistingTest(){
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> subjectService.findByDepartment(1L));
    }
    @Test
    public void findByDepartmentEmptyListTest(){
        Department department = new Department(1L,"Department");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        when(subjectRepository.findByDepartmentId(department.getId())).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class,()-> subjectService.findByDepartment(1L));
    }
}
