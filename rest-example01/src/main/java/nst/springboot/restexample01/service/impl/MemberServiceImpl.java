package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.service.AcademicTitleHistoryService;
import nst.springboot.restexample01.service.MemberService;
import nst.springboot.restexample01.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.domain.*;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

        Member member = new Member();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        AcademicTitle academicTitle1 = academicTitleRepository.findByTitleIgnoreCase(academicTitle)
                .orElseGet(() -> academicTitleRepository.save(new AcademicTitle(academicTitleRepository.findMaxId() + 1, academicTitle)));
        member.setAcademicTitle(academicTitle1);

        EducationTitle educationTitle1 = educationTitleRepository.findByTitleIgnoreCase(educationTitle)
                .orElseGet(() -> educationTitleRepository.save(new EducationTitle(educationTitleRepository.findMaxId() + 1, educationTitle)));
        member.setEducationTitle(educationTitle1);

        ScientificField scientificField1 = scientificFieldRepository.findByScfFieldIgnoreCase(scientificField)
                .orElseGet(() -> scientificFieldRepository.save(new ScientificField(scientificFieldRepository.findMaxId() + 1, scientificField)));
        member.setScientificField(scientificField1);

        Department department1 = departmentRepository.findByNameIgnoreCase(department)
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
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        Member member = memberRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Member does not exist!"));
        List<AcademicTitleHistory> academicTitleHistoryList = academicTitleHistoryRepository.findByMemberId(id);
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

        AcademicTitle academicTitle = academicTitleRepository.findByTitleIgnoreCase(memberDto.getAcademicTitle())
                .orElseGet(() -> academicTitleRepository.save(new AcademicTitle(academicTitleRepository.findMaxId() + 1, memberDto.getAcademicTitle())));
        if(!memberDto.getAcademicTitle().equals(member.getAcademicTitle().getTitle())){
            academicTitleHistoryService.save(member.getId(), LocalDate.now(),memberDto.getAcademicTitle());
        }
        member.setAcademicTitle(academicTitle);

        EducationTitle educationTitle = educationTitleRepository.findByTitleIgnoreCase(memberDto.getEducationTitle())
                .orElseGet(() -> educationTitleRepository.save(new EducationTitle(educationTitleRepository.findMaxId() + 1, memberDto.getEducationTitle())));
        member.setEducationTitle(educationTitle);

        ScientificField scientificField = scientificFieldRepository.findByScfFieldIgnoreCase(memberDto.getScientificField())
                .orElseGet(() -> scientificFieldRepository.save(new ScientificField(scientificFieldRepository.findMaxId() + 1, memberDto.getScientificField())));
        member.setScientificField(scientificField);

        Department department = departmentRepository.findByNameIgnoreCase(memberDto.getDepartment())
                .orElseThrow(() -> new EntityNotFoundException("Department with name " + memberDto.getDepartment() + " not found."));
        member.setDepartment(department);

        memberRepository.save(member);
    }

    @Override
    public MemberDto findById(Long id) throws Exception {
        Member member = memberRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Member does not exist!"));
        return memberConverter.toDto(member);

    }

    @Override
    public List<AcademicTitleHistoryDto> getHistory(Long id) throws Exception {
        memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Member does not exist!"));
        List<AcademicTitleHistory> history = academicTitleHistoryRepository.findByMemberId(id);
        if(history.isEmpty()){throw new NullPointerException("There are no academic title history for this member");}
        return history
                .stream().map(academicTitleHistory -> academicTitleHistoryConverter.toDto(academicTitleHistory))
                .collect(Collectors.toList());
    }


    @Override
    public List<MemberDto> getAllByDepartment(Long id) throws Exception {

        Department check = departmentRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Department is not exist!"));

        List<Member> list = memberRepository.findByDepartmentId(id);
        if(list.isEmpty()){
            throw new NullPointerException("There are no members witch department is "+check.getName()+"!");
        }
        return list.stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());
    }

    @Override
    public List<MemberDto> getAllByAcademicTitle(Long id) throws Exception {
        AcademicTitle academicTitle = academicTitleRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Academic title is not exist!"));

        List<MemberDto> list = memberRepository.findByAcademicTitleId(id)
                .stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());
        if(list.isEmpty()){
            throw new NullPointerException("There are no members witch academic title is "+ academicTitle.getTitle() +"!");
        }
        return list;
    }

    @Override
    public List<MemberDto> getAllByEducationTitle(Long id) throws Exception {
        EducationTitle educationTitle = educationTitleRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Education title is not exist!"));

        List<MemberDto> list = memberRepository.findByEducationTitleId(id)
                .stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());

        if(list.isEmpty()){
            throw new NullPointerException("There are no members witch education title is " + educationTitle.getTitle() + "!");
        }
        return list;
    }

    @Override
    public List<MemberDto> getAllByScientificField(Long id) throws Exception {
        ScientificField scfField = scientificFieldRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Scientific field is not exist!"));

        List<MemberDto> list = memberRepository.findByScientificFieldId(id)
                .stream().map(member -> memberConverter.toDto(member)).collect(Collectors.toList());

        if(list.isEmpty()){
            throw new NullPointerException("There are no members witch scientific field is " + scfField.getScfField() + "!");
        }
        return list;
    }
}
