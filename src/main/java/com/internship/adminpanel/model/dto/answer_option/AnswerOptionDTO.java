package com.internship.adminpanel.model.dto.answer_option;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerOptionDTO {

    private String answer;

    @JsonProperty
    private boolean isCorrect;

}
