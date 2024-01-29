package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.controller.domain.AcademicTitleHistory;
import nst.springboot.restexample01.controller.repository.AcademicTitleRepository;
import nst.springboot.restexample01.controller.repository.ScientificFieldRepository;
import nst.springboot.restexample01.converter.DtoEntityConverter;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AcademicTitleHistoryConverter implements DtoEntityConverter<AcademicTitleHistoryDto, AcademicTitleHistory> {
    @Autowired
    private MemberConverter memberConverter;
    @Autowired
    private AcademicTitleRepository academicTitleRepository;
    @Autowired
    private ScientificFieldRepository scientificFieldRepository;

    @Override
    public AcademicTitleHistoryDto toDto(AcademicTitleHistory academicTitleHistory) {
        return new AcademicTitleHistoryDto(academicTitleHistory.getId(),
                memberConverter.toDto(academicTitleHistory.getMember()),
                academicTitleHistory.getStartDate(),
                academicTitleHistory.getEndDate(),
                academicTitleHistory.getAcademicTitle().getTitle(),
                academicTitleHistory.getScientificField().getScfField());
    }

    @Override
    public AcademicTitleHistory toEntity(AcademicTitleHistoryDto academicTitleHistoryDto) {
        return new AcademicTitleHistory(academicTitleHistoryDto.getId(),
                memberConverter.toEntity(academicTitleHistoryDto.getMemberDto()),
                academicTitleHistoryDto.getStartDate(),
                academicTitleHistoryDto.getEndDate(),
                academicTitleRepository.findByTitleIgnoreCase(academicTitleHistoryDto.getAcademicTitle()).get(),
                scientificFieldRepository.findByScfFieldIgnoreCase(academicTitleHistoryDto.getScientificField()).get());
    }
}
