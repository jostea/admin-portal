package com.internship.adminpanel.model.dto.test_report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateTestReportDTO {

    private Long candidateId;
    private SkillsResultsDTO skillsResultsDTO;
    private SingleChoiceResultsDTO singleChoiceResultsDTO;
    private MultiChoiceResultsDTO multiChoiceResultsDTO;
    private CustomChoiceResultsDTO customChoiceResultsDTO;
    private SqlTasksResultsDTO sqlResultsDTO;
//    private CodeTasksResultsDTO codeTasksResultsDTO;

}
