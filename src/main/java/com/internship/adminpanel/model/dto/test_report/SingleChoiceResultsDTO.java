package com.internship.adminpanel.model.dto.test_report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleChoiceResultsDTO {

    private List<SingleChoiceAnswerDTO> singleChoiceAnswers;

}
