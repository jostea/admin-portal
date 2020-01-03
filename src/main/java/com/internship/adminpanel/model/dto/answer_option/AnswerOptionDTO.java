package com.internship.adminpanel.model.dto.answer_option;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.AnswersOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerOptionDTO {

    private Long id;

    private String answer;

    @JsonProperty
    private boolean isCorrect;

    public AnswerOptionDTO(AnswersOption entity){
        this.setId(entity.getId());
        this.setAnswer(entity.getAnswerOptionValue());
        this.setCorrect(entity.isCorrect());
    }



}
