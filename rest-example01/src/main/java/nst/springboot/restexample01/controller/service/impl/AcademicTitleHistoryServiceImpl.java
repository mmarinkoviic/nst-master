package nst.springboot.restexample01.controller.service.impl;

import nst.springboot.restexample01.controller.domain.AcademicTitle;
import nst.springboot.restexample01.controller.domain.AcademicTitleHistory;
import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.domain.ScientificField;
import nst.springboot.restexample01.controller.repository.AcademicTitleHistoryRepository;
import nst.springboot.restexample01.controller.repository.AcademicTitleRepository;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.repository.ScientificFieldRepository;
import nst.springboot.restexample01.controller.service.AcademicTitleHistoryService;
import nst.springboot.restexample01.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcademicTitleHistoryServiceImpl implements AcademicTitleHistoryService {

    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    private AcademicTitleHistoryConverter academicTitleHistoryConverter;
    private ScientificFieldRepository scientificFieldRepository;
    private MemberRepository memberRepository;
    private AcademicTitleRepository academicTitleRepository;

    public AcademicTitleHistoryServiceImpl(AcademicTitleHistoryRepository academicTitleHistoryRepository, AcademicTitleHistoryConverter academicTitleHistoryConverter,
                                           ScientificFieldRepository scientificFieldRepository, MemberRepository memberRepository,
                                           AcademicTitleRepository academicTitleRepository) {
        this.academicTitleHistoryRepository = academicTitleHistoryRepository;
        this.academicTitleHistoryConverter = academicTitleHistoryConverter;
        this.scientificFieldRepository = scientificFieldRepository;
        this.memberRepository = memberRepository;
        this.academicTitleRepository = academicTitleRepository;
    }

    @Transactional
    @Override
    public AcademicTitleHistoryDto save(String firstName, String lastName, LocalDate startDate,
                                        String academicTitle) throws Exception {
        Member member = memberRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new Exception("Member " + firstName + " " + lastName + " does not exist!"));
        if(!startDate.isEqual(LocalDate.now()) && !startDate.isAfter(LocalDate.now())){throw new Exception("Date is not valid!");}

        String scientificField = member.getScientificField().getScfField();

        AcademicTitle academicTitleObj = academicTitleRepository.findByTitle(academicTitle)
                .orElseThrow(() -> new Exception("Academic title " + academicTitle + " does not exist!"));

        ScientificField scientificFieldObj = scientificFieldRepository.findByScfField(scientificField)
                .orElseThrow(() -> new Exception("Scientific field " + scientificField + " does not exist!"));

        Optional<AcademicTitleHistory> check = academicTitleHistoryRepository.findByMemberFirstNameAndMemberLastNameAndAcademicTitleTitleAndScientificFieldScfField
                (firstName,lastName,academicTitle,scientificField);

        if(check.isPresent()){
            throw new Exception
                    ("Member " + firstName + " " + lastName + " with academic title " + academicTitle + " in scientific field " + scientificField + " already exist!");
        }

        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory();
        academicTitleHistory.setMember(member);
        academicTitleHistory.setStartDate(startDate);
        academicTitleHistory.setEndDate(null);
        academicTitleHistory.setAcademicTitle(academicTitleObj);
        academicTitleHistory.setScientificField(scientificFieldObj);

        update(firstName,lastName,startDate);
        member.setAcademicTitle(academicTitleObj);

        memberRepository.save(member);
        AcademicTitleHistory savedHistory = academicTitleHistoryRepository.save(academicTitleHistory);


        return academicTitleHistoryConverter.toDto(savedHistory);
    }

    @Override
    public List<String> getAll() {
        return academicTitleHistoryRepository
                .findAll()
                .stream().map(entity -> printing(academicTitleHistoryConverter.toDto(entity))).sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<AcademicTitleHistory> academicTitleHistory = academicTitleHistoryRepository.findById(id);
        if (academicTitleHistory.isPresent()) {
            AcademicTitleHistory ath = academicTitleHistory.get();
            if(ath.getEndDate() == null){throw new Exception("It can not be deleted! This record is about active statement!");}
            academicTitleHistoryRepository.delete(ath);
        } else {
            throw new Exception("Academic title history does not exist!");
        }
    }

    @Override
    public void update(String firstName, String lastName, LocalDate endDate) throws Exception {
        Member member = memberRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new Exception("Member " + firstName + " " + lastName + " does not exist!"));

        Optional<AcademicTitleHistory> academicTitleHistory = academicTitleHistoryRepository
                .findByEndDateIsNullAndMemberId(member.getId());
        if(academicTitleHistory.isPresent()){
            AcademicTitleHistory academicTitleHistoryUpdate = academicTitleHistory.get();
            academicTitleHistoryUpdate.setEndDate(endDate);
            academicTitleHistoryRepository.save(academicTitleHistoryUpdate);
        }

    }

    @Override
    public String findById(Long id) throws Exception {
        Optional<AcademicTitleHistory> academicTitleHistory = academicTitleHistoryRepository.findById(id);
        if (academicTitleHistory.isPresent()) {
            AcademicTitleHistory ath = academicTitleHistory.get();
            return this.printing(academicTitleHistoryConverter.toDto(ath));
        } else {
            throw new Exception("Academic title history does not exist!");
        }
    }

    @Override
    public List<String> getAllByMember(String firstName, String lastName) throws Exception{
        Optional<Member> member = memberRepository.findByFirstNameAndLastName(firstName,lastName);
        if(member.isEmpty()){
            throw new Exception("Member " + firstName + " " + lastName + " does not exist!");
        }
        List<AcademicTitleHistory> list = academicTitleHistoryRepository.findByMemberFirstNameAndMemberLastName(firstName,lastName);
        List<String> print = list.stream().map(academicTitleHistory -> printing(academicTitleHistoryConverter.toDto(academicTitleHistory))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new Exception("There are no records about member " +firstName+" "+lastName);
        }
        return print;
    }

    @Override
    public List<String> getAllCurrently() {
        List<AcademicTitleHistory> list = academicTitleHistoryRepository.findByEndDateIsNull();
        List<String> print = list.stream().map(academicTitleHistory -> printing(academicTitleHistoryConverter.toDto(academicTitleHistory))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active members.");
        }
        return print;
    }

    @Override
    public List<String> getAllByScfField(String scientificField) throws Exception{
        Optional<ScientificField> scfId = scientificFieldRepository.findByScfField(scientificField);
        if (scfId.isEmpty()){
            throw new Exception("Scientific field " + scientificField + " does not exist!");
        }
        List<AcademicTitleHistory> list = academicTitleHistoryRepository.findByScientificFieldScfField(scientificField);
        List<String> print = list.stream().map(academicTitleHistory -> printing(academicTitleHistoryConverter.toDto(academicTitleHistory))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new Exception("There are no records about scientific field " + scientificField);
        }
        return print;
    }

    @Override
    public List<String> getAllCurrentlyByAcademicTitle(String academicTitle) throws Exception{
        academicTitleRepository.findByTitle(academicTitle).orElseThrow(()->new Exception("Academic title " + academicTitle + " does not exist!"));
        List<AcademicTitleHistory> list = academicTitleHistoryRepository.findByEndDateIsNullAndAcademicTitleTitle(academicTitle);
        List<String> print = list.stream().map(academicTitleHistory -> printing(academicTitleHistoryConverter.toDto(academicTitleHistory))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new Exception("There are no records about currently active members with academic title " + academicTitle);
        }
        return print;
    }

    @Override
    public String printing(AcademicTitleHistoryDto academicTitleHistoryDto) {
        if (academicTitleHistoryDto.getEndDate() != null) {
            return academicTitleHistoryDto.getMemberDto().toString() + " was " + academicTitleHistoryDto.getAcademicTitle()
                    + " from " + academicTitleHistoryDto.getStartDate() + " to " + academicTitleHistoryDto.getEndDate() +
                    " within scientific field " + academicTitleHistoryDto.getScientificField();
        }else{
            return academicTitleHistoryDto.getMemberDto().toString() + " become " + academicTitleHistoryDto.getAcademicTitle()
                    + " from " + academicTitleHistoryDto.getStartDate() + " within scientific field " + academicTitleHistoryDto.getScientificField();
        }
    }
}
