package nst.springboot.restexample01.controller.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    public AcademicTitleHistoryDto save(Long memberId, LocalDate startDate, String academicTitle) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member does not exist!"));

        if(!startDate.isEqual(LocalDate.now()) && !startDate.isBefore(LocalDate.now())){throw new IllegalArgumentException("Date is not valid! It can not be date from future!");}

        String scientificField = member.getScientificField().getScfField();

        AcademicTitle academicTitleObj = academicTitleRepository.findByTitleIgnoreCase(academicTitle)
                .orElseThrow(() -> new EntityNotFoundException("Academic title " + academicTitle + " does not exist!"));

        ScientificField scientificFieldObj = scientificFieldRepository.findByScfFieldIgnoreCase(scientificField).get();

        Optional<AcademicTitleHistory> check = academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(memberId,academicTitle);

        if(check.isPresent()){
            throw new EntityExistsException
                    ("Member with " + memberId + " with academic title " + academicTitle + " in scientific field " + scientificField + " already exist!");
        }

        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory();
        academicTitleHistory.setMember(member);
        academicTitleHistory.setStartDate(startDate);
        academicTitleHistory.setEndDate(null);
        academicTitleHistory.setAcademicTitle(academicTitleObj);
        academicTitleHistory.setScientificField(scientificFieldObj);

        update(member.getId(),startDate);
        member.setAcademicTitle(academicTitleObj);

        memberRepository.save(member);
        AcademicTitleHistory savedHistory = academicTitleHistoryRepository.save(academicTitleHistory);


        return academicTitleHistoryConverter.toDto(savedHistory);
    }

    @Override
    public AcademicTitleHistoryDto savePrevious(Long memberId, LocalDate startDate, LocalDate endDate, String academicTitle) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member does not exist!"));
        if(!startDate.isEqual(endDate) && !startDate.isBefore(endDate)){throw new IllegalArgumentException("End date must be after start date!");}

        String scientificField = member.getScientificField().getScfField();

        AcademicTitle academicTitleObj = academicTitleRepository.findByTitleIgnoreCase(academicTitle)
                .orElseThrow(() -> new EntityNotFoundException("Academic title " + academicTitle + " does not exist!"));

        ScientificField scientificFieldObj = scientificFieldRepository.findByScfFieldIgnoreCase(scientificField).get();

        Optional<AcademicTitleHistory> check = academicTitleHistoryRepository.findByMemberIdAndAcademicTitleTitle(memberId,academicTitle);

        if(check.isPresent()){
            throw new EntityExistsException
                    ("Member with " + memberId + " with academic title " + academicTitle + " in scientific field " + scientificField + " already exist!");
        }

        AcademicTitleHistory current = academicTitleHistoryRepository.findByEndDateIsNullAndMemberId(memberId).get();

        if(endDate.isAfter(current.getStartDate())){
            throw new IllegalArgumentException("Dates are not valid! Completed record is later than current one according to dates!");
        }

        AcademicTitleHistory academicTitleHistory = new AcademicTitleHistory();
        academicTitleHistory.setMember(member);
        academicTitleHistory.setStartDate(startDate);
        academicTitleHistory.setEndDate(endDate);
        academicTitleHistory.setAcademicTitle(academicTitleObj);
        academicTitleHistory.setScientificField(scientificFieldObj);

        AcademicTitleHistory savedHistory = academicTitleHistoryRepository.save(academicTitleHistory);

        return academicTitleHistoryConverter.toDto(savedHistory);
    }

    @Override
    public List<AcademicTitleHistoryDto> getAll() {
        return academicTitleHistoryRepository
                .findAll()
                .stream().map(entity -> academicTitleHistoryConverter.toDto(entity))
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
    public void update(Long id, LocalDate endDate) throws Exception {
        memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Member does not exist!"));

        Optional<AcademicTitleHistory> academicTitleHistory = academicTitleHistoryRepository
                .findByEndDateIsNullAndMemberId(id);
        if(academicTitleHistory.isPresent()){
            AcademicTitleHistory academicTitleHistoryUpdate = academicTitleHistory.get();
            academicTitleHistoryUpdate.setEndDate(endDate);
            academicTitleHistoryRepository.save(academicTitleHistoryUpdate);
        }
    }

    @Override
    public AcademicTitleHistoryDto findById(Long id) throws Exception {
        AcademicTitleHistory academicTitleHistory = academicTitleHistoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Academic title history does not exist!"));
       return academicTitleHistoryConverter.toDto(academicTitleHistory);
    }

    @Override
    public List<AcademicTitleHistoryDto> getAllByMember(Long id) throws Exception{
        Member member = memberRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Member does not exist!"));

        List<AcademicTitleHistoryDto> list = academicTitleHistoryRepository.findByMemberId(id).stream().map(academicTitleHistory -> academicTitleHistoryConverter.toDto(academicTitleHistory)).collect(Collectors.toList());
        if(list.isEmpty()){
            throw new NullPointerException("There are no records about member " +member.getFirstName()+" "+member.getLastName());
        }
        return list;
    }

}
