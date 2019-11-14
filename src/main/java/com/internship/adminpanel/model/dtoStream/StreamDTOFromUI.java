package com.internship.adminpanel.model.dtoStream;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamDTOFromUI {
    private Long id;

    private String streamName;

    private String disciplineName;
}
