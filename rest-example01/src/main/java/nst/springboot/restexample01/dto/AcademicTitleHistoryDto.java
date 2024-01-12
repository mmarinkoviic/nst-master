package nst.springboot.restexample01.dto;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;


public class AcademicTitleHistoryDto implements Serializable {
    private Long id;
    private MemberDto memberDto;
    @NotEmpty(message = "Start date is obligatory.")
    private LocalDate startDate;
    private LocalDate endDate;
    private String academicTitle;
    private String scientificField;

    public AcademicTitleHistoryDto(){

    }

    public AcademicTitleHistoryDto(Long id, MemberDto memberDto, LocalDate startDate, LocalDate endDate,
                                   String academicTitle, String scientificField) {

        if(endDate != null && startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        this.id = id;
        this.memberDto = memberDto;
        this.startDate = startDate;
        this.endDate = endDate;
        this.academicTitle = academicTitle;
        this.scientificField = scientificField;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public void setMemberDto(MemberDto memberDto) {
        this.memberDto = memberDto;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if(this.endDate != null && startDate.isAfter(this.endDate)){
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if(endDate != null && endDate.isBefore(this.startDate)){
            throw new IllegalArgumentException("End date must be after or equal to start date.");
        }
        this.endDate = endDate;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getScientificField() {
        return scientificField;
    }

    public void setScientificField(String scientificField) {
        this.scientificField = scientificField;
    }
}
