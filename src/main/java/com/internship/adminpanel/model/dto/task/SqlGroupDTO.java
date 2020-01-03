package com.internship.adminpanel.model.dto.task;

import com.internship.adminpanel.model.SqlGroup;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SqlGroupDTO {

    private Long id;
    private String name;
    private String imagePath;

    public SqlGroupDTO(SqlGroup entity){
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setImagePath(entity.getImagePath());
    }

}
