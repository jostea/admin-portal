package com.internship.adminpanel.model.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SqlGroupDTO {

    private Long id;
    private String name;
    private String imagePath;

}
