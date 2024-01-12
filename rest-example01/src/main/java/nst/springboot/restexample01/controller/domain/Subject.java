package nst.springboot.restexample01.controller.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "tbl_subject")
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is an obligatory field!")
    @Size(min = 2, max = 100, message = "Length of the name must be in the range from 2 to 100!")
    @Column(name = "name")
    private String name;
    @Positive(message = "Espb is an obligatory field!")
    @Column(name = "espb")
    private int esbp;

    //@NotEmpty(message = "Department is required for each subject!")
    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;

    public Subject() {
    }

    public Subject(Long id, String name, int esbp, Department department) {
        this.id = id;
        this.name = name;
        this.esbp = esbp;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEsbp() {
        return esbp;
    }

    public void setEsbp(int esbp) {
        this.esbp = esbp;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Subject " + name + " (worth " + esbp + " esbp, part of department " + department.getName() + ")";
    }
}
