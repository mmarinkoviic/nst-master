package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.EducationTitle;
import nst.springboot.restexample01.converter.DtoEntityConverter;
import nst.springboot.restexample01.dto.EducationTitleDto;
import org.springframework.stereotype.Component;

@Component
public class EducationTitleConverter implements DtoEntityConverter<EducationTitleDto, EducationTitle> {

    @Override
    public EducationTitleDto toDto(EducationTitle educationTitle) {
        return new EducationTitleDto(educationTitle.getId(), educationTitle.getTitle());
    }

    @Override
    public EducationTitle toEntity(EducationTitleDto educationTitleDto) {
        return new EducationTitle(educationTitleDto.getId(), educationTitleDto.getTitle());
    }
}
