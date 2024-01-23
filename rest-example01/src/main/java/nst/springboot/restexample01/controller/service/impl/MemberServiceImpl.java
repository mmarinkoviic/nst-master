package nst.springboot.restexample01.controller.service.impl;

import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.controller.domain.*;
import nst.springboot.restexample01.controller.repository.*;
import nst.springboot.restexample01.controller.service.AcademicTitleHistoryService;
import nst.springboot.restexample01.controller.service.MemberService;
import nst.springboot.restexample01.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.restexample01.converter.impl.ManagementConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;
    private AcademicTitleRepository academicTitleRepository;
    private EducationTitleRepository educationTitleRepository;
    private ScientificFieldRepository scientificFieldRepository;
    private DepartmentRepository departmentRepository;
    private MemberConverter memberConverter;
    private ManagementRepository managementRepository;
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    private AcademicTitleHistoryService academicTitleHistoryService;
    private AcademicTitleHistoryConverter academicTitleHistoryConverter;
    public MemberServiceImpl (MemberRepository memberRepository, MemberConverter memberConverter,
                              AcademicTitleRepository academicTitleRepository,
                              EducationTitleRepository educationTitleRepository,
                              ScientificFieldRepository scientificFieldRepository,
                              DepartmentRepository departmentRepository,
                              ManagementRepository managementRepository,
                              AcademicTitleHistoryRepository academicTitleHistoryRepository,
                              AcademicTitleHistoryService academicTitleHistoryService,
                              AcademicTitleHistoryConverter academicTitleHistoryConverter){
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
        this.academicTitleRepository = academicTitleRepository;
        this.educationTitleRepository = educationTitleRepository;
        this.scientificFieldRepository = scientificFieldRepository;
        this.departmentRepository = departmentRepository;
        this.managementRepository = managementRepository;
        this.academicTitleHistoryRepository = academicTitleHistoryRepository;
        this.academicTitleHistoryService = academicTitleHistoryService;
        this.academicTitleHistoryConverter = academicTitleHistoryConverter;
    }

    @Override
    @Transactional
    public MemberDto save(String firstName, String lastName, String academicTitle, String educationTitle, String
            scientificField, String department) throws Exception {
        if(memberRepository.findByFirstNameAndLastName(firstName,lastName).isPresent()){throw new Exception("Member already exist!");}
        Member member = new Member();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        AcademicTitle academicTitle1 = academicTitleRepository.findByTitle(academicTitle)
                .orElseGet(() -> academicTitleRepository.save(new AcademicTitle(academicTitleRepository.count() + 1, academicTitle)));
        member.setAcademicTitle(academicTitle1);

        EducationTitle educationTitle1 = educationTitleRepository.findByTitle(educationTitle)
                .orElseGet(() -> educationTitleRepository.save(new EducationTitle(educationTitleRepository.count() + 1, educationTitle)));
        member.setEducationTitle(educationTitle1);

        ScientificField scientificField1 = scientificFieldRepository.findByScfField(scientificField)
                .orElseGet(() -> scientificFieldRepository.save(new ScientificField(scientificFieldRepository.count() + 1, scientificField)));
        member.setScientificField(scientificField1);

        Department department1 = departmentRepository.findByName(department)
                .orElseThrow(() -> new EntityNotFoundException("Department with name " + department + " not found."));
        member.setDepartment(department1);

        Member saved = memberRepository.save(member);
        academicTitleHistoryService.save(saved.getId(),LocalDate.now(),academicTitle1.getTitle());
        return memberConverter.toDto(saved);
    }

    @Override
    public List<MemberDto> getAll() {
        return memberRepository
                .findAll()
                .stream().map(entity -> memberConverter.toDto(entity))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        Member member = memberRepository.findById(id).orElseThrow(()->new Exception("Member does not exist!"));
        List<AcademicTitleHistory> academicTitleHistoryList = academicTitleHistoryRepository.findByMemberFirstNameAndMemberLastName(member.getFirstName(),member.getLastName());
        List<Management> managementList = managementRepository.findByMemberId(id);
        if(academicTitleHistoryList.isEmpty() && managementList.isEmpty()) {
            memberRepository.delete(member);
        }else{
            throw new Exception("Member can not be deleted! In order to delete member, his/hers history must be deleted!");
        }
    }

    @Override
    public void update(MemberDto memberDto) throws Exception {

        Long id = memberDto.getId();
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member with id " + id + " not found."));

        member.setFirstName(memberDto.getFirstName());
        member.setLastName(memberDto.getLastName());

        AcademicTitle academicTitle = academicTitleRepository.findByTitle(memberDto.getAcademicTitle())
                .orElseGet(() -> academicTitleRepository.save(new AcademicTitle(academicTitleRepository.count() + 1, memberDto.getAcademicTitle())));
        if(!memberDto.getAcademicTitle().equals(member.getAcademicTitle().getTitle())){
            academicTitleHistoryService.save(member.getId(), LocalDate.now(),memberDto.getAcademicTitle());
        }
        member.setAcademicTitle(academicTitle);

        EducationTitle educationTitle = educationTitleRepository.findByTitle(memberDto.getEducationTitle())
                .orElseGet(() -> educationTitleRepository.save(new EducationTitle(educationTitleRepository.count() + 1, memberDto.getEducationTitle())));
        member.setEducationTitle(educationTitle);

        ScientificField scientificField = scientificFieldRepository.findByScfField(memberDto.getScientificField())
                .orElseGet(() -> scientificFieldRepository.save(new ScientificField(scientificFieldRepository.count() + 1, memberDto.getScientificField())));
        member.setScientificField(scientificField);

        Department department = departmentRepository.findByName(memberDto.getDepartment())
                .orElseThrow(() -> new EntityNotFoundException("Department with name " + memberDto.getDepartment() + " not found."));
        member.setDepartment(department);

        memberRepository.save(member);
    }

    @Override
    public MemberDto findById(Long id) throws Exception {
        Member member = memberRepository.findById(id).orElseThrow(()->new Exception("Member does not exist!"));
        return memberConverter.toDto(member);

    }

    @Override
    public List<AcademicTitleHistoryDto> getHistory(Long id) throws Exception {
        memberRepository.findById(id).orElseThrow(()->new Exception("Member does not exist!"));
        List<AcademicTitleHistory> history = academicTitleHistoryRepository.findByMemberId(id);
        if(history.isEmpty()){throw new Exception("There are no academic title history for this member");}
        return history
                .stream().map(academicTitleHistory -> academicTitleHistoryConverter.toDto(academicTitleHistory))
                .collect(Collectors.toList());
    }


    @Override
    public List<MemberDto> getAllByDepartment(Long id) throws Exception {

        Optional<Department> check = departmentRepository.findById(id);
        if(check.isEmpty()){
            throw new Exception("Department " + check.get().getName() + " is not exist!");
        }
        List<Member> list = memberRepository.findByDepartmentId(id);
        if(list.isEmpty()){
            throw new Exception("There are no members witch department is "+check.get().getName()+"!");
        }
        return list.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());
    }

    @Override
    public List<MemberDto> getAllByAcademicTitle(Long id) throws Exception {
        Optional<AcademicTitle> academicT = academicTitleRepository.findById(id);
        if(academicT.isEmpty()){
            throw new Exception("Academic title is not exist!");
        }

        List<Member> list = memberRepository.findByAcademicTitleId(id);
        List<MemberDto> print = list.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new Exception("There are no members witch academic title is "+ academicT.get().getTitle() +"!");
        }
        return print;
    }

    @Override
    public List<MemberDto> getAllByEducationTitle(Long id) throws Exception {
        Optional<EducationTitle> educationT = educationTitleRepository.findById(id);
        if(educationT.isEmpty()){
            throw new Exception("Education title is not exist!");
        }
        List<Member> list = memberRepository.findByEducationTitleId(id);
        List<MemberDto> print = list.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new Exception("There are no members witch education title is " + educationT.get().getTitle() + "!");
        }
        return print;
    }

    @Override
    public List<MemberDto> getAllByScientificField(Long id) throws Exception {
        Optional<ScientificField> scfField = scientificFieldRepository.findById(id);
        if(scfField.isEmpty()){
            throw new Exception("Scientific field is not exist!");
        }
        List<Member> list = memberRepository.findByScientificFieldId(id);
        List<MemberDto> print = list.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new Exception("There are no members witch scientific field is " + scfField.get().getScfField() + "!");
        }
        return print;
    }
}
