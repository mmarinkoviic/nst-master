package nst.springboot.restexample01.controller.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
@Table(name = "tbl_education_title")
public class EducationTitle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is an obligatory field!")
    @Column(name = "title")
    private String title;

    public EducationTitle () {}

    public EducationTitle (Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
