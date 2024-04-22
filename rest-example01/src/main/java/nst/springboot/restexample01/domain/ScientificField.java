package nst.springboot.restexample01.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScientificField that = (ScientificField) o;
        return Objects.equals(id, that.id) && Objects.equals(scfField, that.scfField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scfField);
    }
}
