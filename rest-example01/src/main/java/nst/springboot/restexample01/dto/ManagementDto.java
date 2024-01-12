package nst.springboot.restexample01.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class ManagementDto implements Serializable {
    private Long id;
    private String departmentDto;
    private MemberDto memberDto;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;


    public ManagementDto (){

    }

    public ManagementDto(Long id, String departmentDto, MemberDto memberDto, String role, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.departmentDto = departmentDto;
        this.memberDto = memberDto;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentDto() {
        return departmentDto;
    }

    public void setDepartmentDto(String departmentDto) {
        this.departmentDto = departmentDto;
    }

    public MemberDto getMemberDto() { return memberDto; }

    public void setMemberDto(MemberDto memberDto) { this.memberDto = memberDto; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
