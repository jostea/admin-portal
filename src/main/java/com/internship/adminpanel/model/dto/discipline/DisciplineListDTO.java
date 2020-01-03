package com.internship.adminpanel.model.dto.discipline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineListDTO {

    //TODO: REMOVE THIS DTO. INSTEAD OF THIS IS DisciplineDto
    private Long id;
    private String name;

}
