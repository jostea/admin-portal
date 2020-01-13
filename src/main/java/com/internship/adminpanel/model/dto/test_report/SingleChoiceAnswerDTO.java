package com.internship.adminpanel.model.dto.test_report;

import com.internship.adminpanel.model.AnswersOption;
import com.internship.adminpanel.model.CandidateSingleTask;
import com.internship.adminpanel.model.dto.answer_option.AnswerOptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private List<AnswerOptionDTO> allAnswerOptions;

    public SingleChoiceAnswerDTO(CandidateSingleTask singleTask){
        this.setTextSingleChoiceQuestion(singleTask.getTask().getDescription());
        if (singleTask.getAnswersOption() != null) {
            this.setSelectedAnswerOptionText(singleTask.getAnswersOption().getAnswerOptionValue());
            this.setIdSelectedAO(singleTask.getAnswersOption().getId());
        }
        List<AnswerOptionDTO> answerOptionDTOS = new ArrayList<>();
        for (AnswersOption answersOption: singleTask.getTask().getAnswersOptions()) {
            answerOptionDTOS.add(new AnswerOptionDTO(answersOption));
        }
        this.setAllAnswerOptions(answerOptionDTOS);
    }

}
