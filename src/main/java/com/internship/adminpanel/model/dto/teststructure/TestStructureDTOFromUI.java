package com.internship.adminpanel.model.dto.teststructure;

import com.internship.adminpanel.model.TestStructure;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestStructureDTOFromUI {

    private Long streamId;

    private String taskType;

    private String complexity;

    private Long nrQuestions;

}
