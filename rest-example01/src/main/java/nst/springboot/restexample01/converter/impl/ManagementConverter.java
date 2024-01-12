package nst.springboot.restexample01.converter.impl;

import nst.springboot.restexample01.controller.domain.Management;
import nst.springboot.restexample01.controller.repository.DepartmentRepository;
import nst.springboot.restexample01.converter.DtoEntityConverter;
import nst.springboot.restexample01.dto.ManagementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManagementConverter implements DtoEntityConverter<ManagementDto, Management> {
    @Autowired
    private MemberConverter memberConverter;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public ManagementDto toDto(Management management) {
        return new ManagementDto(management.getId(),management.getDepartment().getName(),
                memberConverter.toDto(management.getMember()),management.getRole(),management.getStartDate(),management.getEndDate());
    }

    @Override
    public Management toEntity(ManagementDto managementDto) {
        return new Management(managementDto.getId(), departmentRepository.findByName(managementDto.getDepartmentDto()).get(),
                memberConverter.toEntity(managementDto.getMemberDto()),managementDto.getRole(),managementDto.getStartDate(),managementDto.getEndDate());
    }
}
