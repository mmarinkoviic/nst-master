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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ManagementRepositoryTests {
    @Autowired
    private ManagementRepository managementRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AcademicTitleRepository academicTitleRepository;
    @Autowired
    private EducationTitleRepository educationTitleRepository;
    @Autowired
    private  ScientificFieldRepository scientificFieldRepository;

    @BeforeEach
    public void setUp(){
        managementRepository.deleteAll();
    }

    @Test
    public void findByDepartmentIdTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"secretary", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management2);
        Management management3 = new Management(3L,department2,member3,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management3);

        List<Management> expectedList = Arrays.asList(management1,management2);
        List<Management> returnedList = managementRepository.findByDepartmentId(department1.getId());

        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByRoleTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"secretary", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management2);
        Management management3 = new Management(3L,department2,member3,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management3);

        List<Management> expectedList = Arrays.asList(management1,management3);
        List<Management> returnedList = managementRepository.findByRole("handler");

        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByEndDateIsNullAndRoleTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management2);
        Management management3 = new Management(3L,department2,member3,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management3);

        List<Management> expectedList = Arrays.asList(management2,management3);
        List<Management> returnedList = managementRepository.findByEndDateIsNullAndRole("handler");

        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByEndDateIsNullAndDepartmentIdTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department1);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"secretary", LocalDate.of(2024,5,5),null);
        managementRepository.save(management2);
        Management management3 = new Management(3L,department1,member3,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management3);

        List<Management> expectedList = Arrays.asList(management2,management3);
        List<Management> returnedList = managementRepository.findByEndDateIsNullAndDepartmentId(department1.getId());

        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByDepartmentIdAndRoleTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department1);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management2);
        Management management3 = new Management(3L,department1,member3,"secretary", LocalDate.of(2024,5,5),null);
        managementRepository.save(management3);

        List<Management> expectedList = Arrays.asList(management1,management2);
        List<Management> returnedList = managementRepository.findByDepartmentIdAndRole(department1.getId(),"handler");

        assertArrayEquals(expectedList.toArray(),returnedList.toArray());

    }
    @Test
    public void findByEndDateIsNullAndDepartmentIdAndRoleTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department2);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department1);
        Member member4 = new Member(4L,"Johnson","Smith",academicTitle,educationTitle,scientificField,department1);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management1);
        Management management2 = new Management(2L,department2,member2,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management2);
        Management management3 = new Management(3L,department1,member3,"secretary", LocalDate.of(2024,5,5),null);
        managementRepository.save(management3);
        Management management4 = new Management(4L,department1,member3,"secretary", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management4);

        Optional<Management> returned = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(department1.getId(),"secretary");

        assertEquals(management3,returned.get());

    }
    @Test
    public void findByMemberIdTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        Department department2 = new Department(2L,"Department2");
        departmentRepository.save(department2);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management2);
        Management management3 = new Management(3L,department1,member1,"secretary", LocalDate.of(2024,5,5),null);
        managementRepository.save(management3);

        List<Management> expectedList = Arrays.asList(management1,management3);
        List<Management> returnedList = managementRepository.findByMemberId(member1.getId());

        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findMaxIdTest(){
        Department department1 = new Department(1L,"Department1");
        departmentRepository.save(department1);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = new Member(1L,"Mary","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member2 = new Member(2L,"Patty","Perry",academicTitle,educationTitle,scientificField,department1);
        Member member3 = new Member(3L,"John","Smith",academicTitle,educationTitle,scientificField,department1);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Management management1 = new Management(1L,department1,member1,"handler", LocalDate.of(2024,5,5),LocalDate.of(2024,6,6));
        managementRepository.save(management1);
        Management management2 = new Management(2L,department1,member2,"handler", LocalDate.of(2024,5,5),null);
        managementRepository.save(management2);
        Management management3 = new Management(3L,department1,member3,"secretary", LocalDate.of(2024,5,5),null);
        managementRepository.save(management3);

        Long maxID = managementRepository.findMaxId();
        assertEquals(management3.getId(),maxID);
    }

}
