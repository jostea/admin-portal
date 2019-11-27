package com.internship.adminpanel.model.dto.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamDTOFromUI {

    private String name;

    private Long disciplineId;
}
