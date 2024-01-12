package nst.springboot.restexample01.controller.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
@Table(name = "tbl_scientific_field")
public class ScientificField implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name of scientific field is obligatory!")
    @Column(name = "scf_field")
    private String scfField;

    public ScientificField() {}
    public ScientificField(Long id, String scfField) {
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
