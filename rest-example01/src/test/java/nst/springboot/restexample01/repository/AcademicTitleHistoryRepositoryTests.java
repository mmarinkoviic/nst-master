package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AcademicTitleHistoryRepositoryTests {
    @Autowired
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AcademicTitleRepository academicTitleRepository;
    @Autowired
    private EducationTitleRepository educationTitleRepository;
    @Autowired
    private ScientificFieldRepository scientificFieldRepository;
    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    public void findByMemberIdTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle1);
        AcademicTitle academicTitle2 = new AcademicTitle(2L,"AT2");
        academicTitleRepository.save(academicTitle2);
        EducationTitle educationTitle1 = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle1);
        ScientificField scientificField1 = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField1);
        Department department1 = new Department(1L,"D1");
        departmentRepository.save(department1);

        Member member1 = new Member(1L,"Mary","Smith",academicTitle1,educationTitle1,scientificField1,department1);
        memberRepository.save(member1);
        Member member2 = new Member(2L,"John","Smith",academicTitle2,educationTitle1,scientificField1,department1);
        memberRepository.save(member2);

        AcademicTitleHistory academicTitleHistory1 = academicTitleHistoryRepository.save(new AcademicTitleHistory(1L,member1, LocalDate.of(2024,5,5),LocalDate.of(2024,6,6),academicTitle1,scientificField1));
        AcademicTitleHistory academicTitleHistory2 = academicTitleHistoryRepository.save(new AcademicTitleHistory(2L,member2, LocalDate.of(2024,5,5),LocalDate.of(2024,6,6),academicTitle1,scientificField1));
        AcademicTitleHistory academicTitleHistory3 = academicTitleHistoryRepository.save(new AcademicTitleHistory(3L,member1, LocalDate.of(2024,6,6),null,academicTitle1,scientificField1));

        List<AcademicTitleHistory> expectedList = Arrays.asList(academicTitleHistory1,academicTitleHistory3);

        List<AcademicTitleHistory> returnedList = academicTitleHistoryRepository.findByMemberId(member1.getId());
        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByEndDateIsNullAndMemberIdTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle1);
        AcademicTitle academicTitle2 = new AcademicTitle(2L,"AT2");
        academicTitleRepository.save(academicTitle2);
        EducationTitle educationTitle1 = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle1);
        ScientificField scientificField1 = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField1);
        Department department1 = new Department(1L,"D1");
        departmentRepository.save(department1);

        Member member1 = new Member(1L,"Mary","Smith",academicTitle1,educationTitle1,scientificField1,department1);
        memberRepository.save(member1);
        Member member2 = new Member(2L,"John","Smith",academicTitle2,educationTitle1,scientificField1,department1);
        memberRepository.save(member2);

        AcademicTitleHistory academicTitleHistory1 = academicTitleHistoryRepository.save(new AcademicTitleHistory(1L,member1, LocalDate.of(2024,5,5),LocalDate.of(2024,6,6),academicTitle1,scientificField1));
        AcademicTitleHistory academicTitleHistory2 = academicTitleHistoryRepository.save(new AcademicTitleHistory(2L,member2, LocalDate.of(2024,5,5),null,academicTitle1,scientificField1));
        AcademicTitleHistory academicTitleHistory3 = academicTitleHistoryRepository.save(new AcademicTitleHistory(3L,member1, LocalDate.of(2024,6,6),null,academicTitle1,scientificField1));

        AcademicTitleHistory returned = academicTitleHistoryRepository.findByEndDateIsNullAndMemberId(member1.getId()).get();

        assertEquals(academicTitleHistory3,returned);
    }
    @Test
    public void findByMemberIdAndAcademicTitleTitleTest(){
        AcademicTitle academicTitle1 = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle1);
        AcademicTitle academicTitle2 = new AcademicTitle(2L,"AT2");
        academicTitleRepository.save(academicTitle2);
        EducationTitle educationTitle1 = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle1);
        ScientificField scientificField1 = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField1);
        Department department1 = new Department(1L,"D1");
        departmentRepository.save(department1);

        Member member1 = new Member(1L,"Mary","Smith",academicTitle1,educationTitle1,scientificField1,department1);
        memberRepository.save(member1);
        Member member2 = new Member(2L,"John","Smith",academicTitle2,educationTitle1,scientificField1,department1);
        memberRepository.save(member2);

        AcademicTitleHistory academicTitleHistory1 = academicTitleHistoryRepository.save(new AcademicTitleHistory(1L,member1, LocalDate.of(2024,5,5),LocalDate.of(2024,6,6),academicTitle2,scientificField1));
        AcademicTitleHistory academicTitleHistory2 = academicTitleHistoryRepository.save(new AcademicTitleHistory(2L,member2, LocalDate.of(2024,5,5),null,academicTitle1,scientificField1));
        AcademicTitleHistory academicTitleHistory3 = academicTitleHistoryRepository.save(new AcademicTitleHistory(3L,member1, LocalDate.of(2024,6,6),null,academicTitle1,scientificField1));

        AcademicTitleHistory returned = academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(member1.getId(),academicTitle2.getTitle()).get();

        assertEquals(academicTitleHistory1,returned);
    }
}
