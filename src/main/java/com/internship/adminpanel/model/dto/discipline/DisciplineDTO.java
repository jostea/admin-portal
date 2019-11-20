package com.internship.adminpanel.model.dto.discipline;

import com.internship.adminpanel.model.Discipline;
import lombok.Data;

@Data
public class DisciplineDTO {

    private Long id;

    private String name;

    public DisciplineDTO(Discipline d) {
        this.id = d.getId();
        this.name = d.getName();
    }
}
