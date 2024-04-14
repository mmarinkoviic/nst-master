package nst.springboot.restexample01.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="tbl_academic_title_history")
public class AcademicTitleHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Member is required!")
    @ManyToOne
    private Member member;
    @NotNull(message = "Start date is obligatory.")
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull(message = "Academic title is required!")
    @ManyToOne
    private AcademicTitle academicTitle;
    @NotNull(message = "Scientific field is required!")
    @ManyToOne
    private ScientificField scientificField;

    public AcademicTitleHistory(){

    }
    public AcademicTitleHistory(Long id, Member member, LocalDate startDate, LocalDate endDate, AcademicTitle academicTitle, ScientificField scientificField) {
        if(endDate != null && startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        this.id = id;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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
        if(endDate!= null && endDate.isBefore(this.startDate)){
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }
        this.endDate = endDate;
    }

    public AcademicTitle getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(AcademicTitle academicTitle) {
        this.academicTitle = academicTitle;
    }

    public ScientificField getScientificField() {
        return scientificField;
    }

    public void setScientificField(ScientificField scientificField) {
        this.scientificField = scientificField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcademicTitleHistory that = (AcademicTitleHistory) o;
        return Objects.equals(id, that.id) && Objects.equals(member, that.member) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(academicTitle, that.academicTitle) && Objects.equals(scientificField, that.scientificField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, startDate, endDate, academicTitle, scientificField);
    }
}
