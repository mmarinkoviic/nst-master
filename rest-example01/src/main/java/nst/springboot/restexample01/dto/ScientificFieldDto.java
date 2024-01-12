package nst.springboot.restexample01.dto;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public class ScientificFieldDto implements Serializable {
    private Long id;
    @NotEmpty(message = "Name of scientific field is obligatory!")
    private String scfField;

    public ScientificFieldDto(){

    }

    public ScientificFieldDto(Long id, String scfField) {
        this.id = id;
        this.scfField = scfField;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getScfField() {
        return scfField;
    }
    public void setScfField(String scfField) {
        this.scfField = scfField;
    }
}
