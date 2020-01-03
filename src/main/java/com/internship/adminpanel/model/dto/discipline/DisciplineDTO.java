package com.internship.adminpanel.model.dto.discipline;

import com.internship.adminpanel.model.Discipline;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineDTO {

    private Long id;

    private String name;

    public DisciplineDTO(Discipline d) {
        this.id = d.getId();
        this.name = d.getName();
    }
}
