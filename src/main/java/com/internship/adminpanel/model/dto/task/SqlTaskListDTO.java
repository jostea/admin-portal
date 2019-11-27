package com.internship.adminpanel.model.dto.task;

import com.internship.adminpanel.model.SqlTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SqlTaskListDTO {

    private Long id;

    private String title;

    private String complexity;

    private Boolean isEnabled;

    public SqlTaskListDTO(SqlTask sqlTask){
        this.setId(sqlTask.getId());
        this.setTitle(sqlTask.getTitle());
        this.setComplexity(sqlTask.getComplexity().getComplexity());
        this.setIsEnabled(sqlTask.isEnabled());
    }

}
