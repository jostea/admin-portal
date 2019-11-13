package com.internship.adminpanel.model.dto;

import com.internship.adminpanel.model.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamDTO {

    private Long id;

    private String name;

    private String disciplineName;

    public StreamDTO(Stream stream) {
        this.id = stream.getId();
        this.name = stream.getName();
        this.disciplineName = stream.getDiscipline().getName();
    }
}
