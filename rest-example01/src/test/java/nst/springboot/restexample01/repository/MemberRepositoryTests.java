package nst.springboot.restexample01.repository;

import nst.springboot.restexample01.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AcademicTitleRepository academicTitleRepository;
    @Autowired
    private EducationTitleRepository educationTitleRepository;
    @Autowired
    private ScientificFieldRepository scientificFieldRepository;



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

        Member member1 = memberRepository.save(new Member(1L,"Katy","Perry",academicTitle,educationTitle,scientificField,department1));
        Member member2 = memberRepository.save(new Member(2L,"Mary","Smith",academicTitle,educationTitle,scientificField,department2));
        Member member3 = memberRepository.save(new Member(3L,"Anna","John",academicTitle,educationTitle,scientificField,department2));
        List<Member> expectedList = Arrays.asList(member2,member3);

        List<Member> returnedList = memberRepository.findByDepartmentId(department2.getId());
        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByAcademicTitleIdTest(){
        Department department = new Department(1L,"Department1");
        departmentRepository.save(department);
        AcademicTitle academicTitle1 = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle1);
        AcademicTitle academicTitle2 = new AcademicTitle(2L,"AT2");
        academicTitleRepository.save(academicTitle2);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = memberRepository.save(new Member(1L,"Katy","Perry",academicTitle1,educationTitle,scientificField,department));
        Member member2 = memberRepository.save(new Member(2L,"Mary","Smith",academicTitle2,educationTitle,scientificField,department));
        Member member3 = memberRepository.save(new Member(3L,"Anna","John",academicTitle2,educationTitle,scientificField,department));
        List<Member> expectedList = Arrays.asList(member2,member3);

        List<Member> returnedList = memberRepository.findByAcademicTitleId(academicTitle2.getId());
        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByEducationTitleIdTest(){
        Department department = new Department(1L,"Department1");
        departmentRepository.save(department);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle1 = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle1);
        EducationTitle educationTitle2 = new EducationTitle(2L,"ET2");
        educationTitleRepository.save(educationTitle2);
        ScientificField scientificField = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField);

        Member member1 = memberRepository.save(new Member(1L,"Katy","Perry",academicTitle,educationTitle1,scientificField,department));
        Member member2 = memberRepository.save(new Member(2L,"Mary","Smith",academicTitle,educationTitle2,scientificField,department));
        Member member3 = memberRepository.save(new Member(3L,"Anna","John",academicTitle,educationTitle2,scientificField,department));
        List<Member> expectedList = Arrays.asList(member2,member3);

        List<Member> returnedList = memberRepository.findByEducationTitleId(educationTitle2.getId());
        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
    @Test
    public void findByScientificFieldIdTest(){
        Department department = new Department(1L,"Department1");
        departmentRepository.save(department);
        AcademicTitle academicTitle = new AcademicTitle(1L,"AT1");
        academicTitleRepository.save(academicTitle);
        EducationTitle educationTitle = new EducationTitle(1L,"ET1");
        educationTitleRepository.save(educationTitle);
        ScientificField scientificField1 = new ScientificField(1L,"ScF1");
        scientificFieldRepository.save(scientificField1);
        ScientificField scientificField2 = new ScientificField(2L,"ScF2");
        scientificFieldRepository.save(scientificField2);

        Member member1 = memberRepository.save(new Member(1L,"Katy","Perry",academicTitle,educationTitle,scientificField1,department));
        Member member2 = memberRepository.save(new Member(2L,"Mary","Smith",academicTitle,educationTitle,scientificField2,department));
        Member member3 = memberRepository.save(new Member(3L,"Anna","John",academicTitle,educationTitle,scientificField2,department));
        List<Member> expectedList = Arrays.asList(member2,member3);

        List<Member> returnedList = memberRepository.findByScientificFieldId(scientificField2.getId());
        assertArrayEquals(expectedList.toArray(),returnedList.toArray());
    }
}
