package nst.springboot.restexample01.dto;

import java.io.Serializable;

public class SubjectDto implements Serializable {
    private Long id;
    private String name;
    private int esbp;
    private String department;

    public SubjectDto() {
    }

    public SubjectDto(Long id, String name, int esbp, String department) {
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

    public void setEsbp(int espb) {
        this.esbp = espb;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Subject " + name + " (worth " + esbp + " esbp, part of department " + department + ")";
    }
    
}
