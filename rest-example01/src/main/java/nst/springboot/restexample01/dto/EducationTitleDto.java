package nst.springboot.restexample01.dto;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public class EducationTitleDto implements Serializable {
    private Long id;

    @NotEmpty(message = "Title is an obligatory field!")
    private String title;

    public EducationTitleDto(){

    }
    public EducationTitleDto(Long id, String title) {
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
