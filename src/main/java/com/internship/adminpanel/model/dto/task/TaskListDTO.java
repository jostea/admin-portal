package com.internship.adminpanel.model.dto.task;

import com.internship.adminpanel.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskListDTO {

    private Long id;

    private String title;

    private String type;

    private String complexity;

    private Boolean isEnabled;

    public TaskListDTO(Task taskEntity){
        this.setId(taskEntity.getId());
        this.setTitle(taskEntity.getTitle());
        this.setType(taskEntity.getTaskType().getType());
        this.setComplexity(taskEntity.getComplexity().getComplexity());
        this.setIsEnabled(taskEntity.isEnabled());
    }

}
