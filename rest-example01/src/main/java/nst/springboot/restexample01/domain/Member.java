package nst.springboot.restexample01.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tbl_member")
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "First name is obligatory field!")
    @Size(min = 2, max = 10, message = "Length of the first name must be in the range from 2 to 10.")
    @Column(name = "first_name")
    private String firstName;
    @NotEmpty(message = "Last name is obligatory field!")
    @Size(min = 2, max = 15, message = "Length of the last name must be in the range from 2 to 15.")
    @Column(name = "last_name")
    private String lastName;
    @NotNull(message = "Academic title is required!")
    @ManyToOne()
    @JoinColumn(name = "academic_title_id")
    private AcademicTitle academicTitle;
    @NotNull(message = "Education title is required!")
    @ManyToOne()
    @JoinColumn(name = "education_title_id")
    private EducationTitle educationTitle;
    @NotNull(message = "Scientific field is required!")
    @ManyToOne()
    @JoinColumn(name = "scf_field_id")
    private ScientificField scientificField;
    @NotNull(message = "Department is required!")
    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;

    public Member () {

    }

    public Member(Long id, String firstName, String lastName, AcademicTitle academicTitle, EducationTitle educationTitle, ScientificField scientificField, Department department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.academicTitle = academicTitle;
        this.educationTitle = educationTitle;
        this.scientificField = scientificField;
        this.department = department;
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

    public AcademicTitle getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(AcademicTitle academicTitle) {
        this.academicTitle = academicTitle;
    }

    public EducationTitle getEducationTitle() {
        return educationTitle;
    }

    public void setEducationTitle(EducationTitle educationTitle) {
        this.educationTitle = educationTitle;
    }

    public ScientificField getScientificField() {
        return scientificField;
    }

    public void setScientificField(ScientificField scientificField) {
        this.scientificField = scientificField;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + educationTitle.getTitle() + ", " + scientificField.getScfField()+ ", " + academicTitle.getTitle() + ", from department " +
        department.getName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(firstName, member.firstName) && Objects.equals(lastName, member.lastName) && Objects.equals(academicTitle, member.academicTitle) && Objects.equals(educationTitle, member.educationTitle) && Objects.equals(scientificField, member.scientificField) && Objects.equals(department, member.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, academicTitle, educationTitle, scientificField, department);
    }
}
