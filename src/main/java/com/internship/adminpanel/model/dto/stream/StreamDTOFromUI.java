package com.internship.adminpanel.model.dto.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamDTOFromUI {

    private String name;

    private Long disciplineId;
}
