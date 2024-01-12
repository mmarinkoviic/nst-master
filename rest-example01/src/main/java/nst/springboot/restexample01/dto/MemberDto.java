package nst.springboot.restexample01.dto;

import java.io.Serializable;

public class MemberDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String academicTitle;
    private String educationTitle;
    private String scientificField;
    private String department;

    public MemberDto(){

    }
    public MemberDto(Long id, String firstName, String lastName, String academicTitle, String educationTitle, String scientificField, String departmentDto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.academicTitle = academicTitle;
        this.educationTitle = educationTitle;
        this.scientificField = scientificField;
        this.department = departmentDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcademicTitle() { return academicTitle; }

    public void setAcademicTitle(String academicTitle) { this.academicTitle = academicTitle; }

    public String getEducationTitle() { return educationTitle; }

    public void setEducationTitle(String educationTitle) { this.educationTitle = educationTitle; }

    public String getScientificField() { return scientificField; }

    public void setScientificField(String scientificField) { this.scientificField = scientificField; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + educationTitle + ", " + scientificField + ", " + academicTitle + ", from department " +
                department + ")";
    }
}
