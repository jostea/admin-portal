package com.internship.adminpanel.model.dto.test_report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CodeTasksResultsDTO {
    List<CodeSingleTaskDTO> codeSingleTaskDTOList;
}
