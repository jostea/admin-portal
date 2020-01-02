package com.internship.adminpanel.model.dto.test_report;

import com.internship.adminpanel.model.CandidateSingleTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleChoiceAnswerDTO {

    private String textSingleChoiceQuestion;
    private Long idSelectedAO;
    private Long idCorrectAO;
    private String selectedAnswerOptionText;
    private String correctAnswerOptionText;
    private boolean isCorrect;

    public SingleChoiceAnswerDTO(CandidateSingleTask singleTask){
        this.setTextSingleChoiceQuestion(singleTask.getTask().getDescription());
        this.setSelectedAnswerOptionText(singleTask.getAnswersOption().getAnswerOptionValue());
        this.setIdSelectedAO(singleTask.getAnswersOption().getId());
    }

}
