package nst.springboot.restexample01.controller.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_management")
public class Management implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Department is required!")
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @NotNull(message = "Member is required!")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @NotNull(message = "Role is required and must be 'manager' or 'secretary'.")
    private String role;
    @NotNull(message = "Start date is obligatory.")
    private LocalDate startDate;
    private LocalDate endDate;

    public Management(){

    }
    public Management(Long id, Department department, Member member, String role, LocalDate startDate, LocalDate endDate){
        if(!role.equals("handler") && !role.equals("secretary")){
            throw new IllegalArgumentException("Role must be secretary or handler.");
        }
        if(endDate != null && startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        if(!member.getDepartment().getId().equals(department.getId())){
            throw new IllegalArgumentException(member.getFirstName() + " " + member.getLastName() + " is not a member of department "+department.getName());
         }
        this.id = id;
        this.department = department;
        this.member = member;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Member getMember() { return member; }

    public void setMember(Member member) {
        if(!member.getDepartment().getId().equals(this.department.getId())){
            throw new IllegalArgumentException(member.getFirstName() + " " + member.getLastName() + " is not a member of department "+this.department.getName());
        }
        this.member = member; }

    public String getRole() { return role; }

    public void setRole(String role) {
        if(!role.equals("handler") && !role.equals("secretary")){
            throw new IllegalArgumentException("Role must be secretary or handler.");
        }
        this.role = role; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) {
        if(endDate != null && startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) {
        if(endDate!= null && endDate.isBefore(this.startDate)){
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }
        this.endDate = endDate; }
}
