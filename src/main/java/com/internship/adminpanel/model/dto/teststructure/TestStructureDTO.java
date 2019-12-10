package com.internship.adminpanel.model.dto.teststructure;

import com.internship.adminpanel.model.TestStructure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestStructureDTO {

    private Long id;

    private String taskType;

    private String complexity;

    private Long nrQuestions;

    public TestStructureDTO(TestStructure testStructure) {
        this.id = testStructure.getId();
        this.complexity = testStructure.getComplexity().getComplexity();
        this.taskType = testStructure.getTaskType().getType();
        this.nrQuestions = testStructure.getNrQuestions();
    }
}
