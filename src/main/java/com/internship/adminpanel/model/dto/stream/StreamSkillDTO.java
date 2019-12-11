package com.internship.adminpanel.model.dto.stream;

import com.internship.adminpanel.model.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamSkillDTO {

    private Long id;

    private String name;

    private String disciplineName;

    public StreamSkillDTO(Stream stream) {
        this.id = stream.getId();
        this.name = stream.getName();
        this.disciplineName = stream.getDiscipline().getName();
    }
}
