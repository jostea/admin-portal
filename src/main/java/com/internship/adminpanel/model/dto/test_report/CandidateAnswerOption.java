package com.internship.adminpanel.model.dto.test_report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateAnswerOption {

    private String answerOptionText;
    private boolean isCorrect;
    private boolean isSelected;

}
