package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.controller.domain.AcademicTitle;
import nst.springboot.restexample01.converter.DtoEntityConverter;
import nst.springboot.restexample01.dto.AcademicTitleDto;
import org.springframework.stereotype.Component;

@Component
public class AcademicTitleConverter implements DtoEntityConverter<AcademicTitleDto, AcademicTitle> {

    @Override
    public AcademicTitleDto toDto(AcademicTitle academicTitle) {
        return new AcademicTitleDto(academicTitle.getId(), academicTitle.getTitle());
    }

    @Override
    public AcademicTitle toEntity(AcademicTitleDto academicTitleDto) {
        return new AcademicTitle(academicTitleDto.getId(), academicTitleDto.getTitle());
    }
}
