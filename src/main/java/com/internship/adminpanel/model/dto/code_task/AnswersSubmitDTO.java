package com.internship.adminpanel.model.dto.code_task;

import lombok.Data;

import java.util.List;

@Data
public class AnswersSubmitDTO {
    private List<InputsDTO> input;
    private InputsDTO output;
}
