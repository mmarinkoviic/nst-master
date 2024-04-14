package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.domain.Member;
import nst.springboot.restexample01.repository.AcademicTitleRepository;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.EducationTitleRepository;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
import nst.springboot.restexample01.converter.DtoEntityConverter;
import nst.springboot.restexample01.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter implements DtoEntityConverter<MemberDto, Member> {
    @Autowired
    private EducationTitleRepository educationTitleRepository;
    @Autowired
    private AcademicTitleRepository academicTitleRepository;
    @Autowired
    private ScientificFieldRepository scientificFieldRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getFirstName(), member.getLastName(), member.getAcademicTitle().getTitle(),
                member.getEducationTitle().getTitle(),member.getScientificField().getScfField(),member.getDepartment().getName());
    }

    @Override
    public Member toEntity(MemberDto memberDto) {
        return new Member(memberDto.getId(), memberDto.getFirstName(), memberDto.getLastName(),
                academicTitleRepository.findByTitleIgnoreCase(memberDto.getAcademicTitle()).get(),
                educationTitleRepository.findByTitleIgnoreCase(memberDto.getEducationTitle()).get(),
                scientificFieldRepository.findByScfFieldIgnoreCase(memberDto.getScientificField()).get(),
                departmentRepository.findByNameIgnoreCase(memberDto.getDepartment()).get());
    }
}
