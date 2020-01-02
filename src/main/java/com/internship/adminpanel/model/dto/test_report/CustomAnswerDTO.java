package com.internship.adminpanel.model.dto.test_report;

import com.internship.adminpanel.model.CandidateCustomTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomAnswerDTO {

    private Long idCandidateCustomTask;
    private String customTaskText;
    private String candidateCustomAnswer;
    private boolean isCorrect;

    public CustomAnswerDTO(CandidateCustomTask candTask){
        this.setIdCandidateCustomTask(candTask.getId());
        this.setCustomTaskText(candTask.getTask().getDescription());
        this.setCandidateCustomAnswer(candTask.getCustomAnswer());
        this.setCorrect(candTask.isCorrect());
    }
}
