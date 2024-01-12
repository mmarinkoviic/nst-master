package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.repository.AcademicTitleRepository;
import nst.springboot.restexample01.controller.repository.DepartmentRepository;
import nst.springboot.restexample01.controller.repository.EducationTitleRepository;
import nst.springboot.restexample01.controller.repository.ScientificFieldRepository;
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
        return new Member(memberDto.getId(), memberDto.getFirstName(), memberDto.getLastName(), academicTitleRepository.findByTitle(memberDto.getAcademicTitle()).get(),
                educationTitleRepository.findByTitle(memberDto.getEducationTitle()).get(),scientificFieldRepository.findByScfField(memberDto.getScientificField()).get(),
                departmentRepository.findByName(memberDto.getDepartment()).get());
    }
}
