package com.internship.adminpanel.model.dto.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamListDTO {

    //TODO: REMOVE THIS DTO. INSTEAD OF THIS IS StreamDto
    private Long id;
    private String name;

}
