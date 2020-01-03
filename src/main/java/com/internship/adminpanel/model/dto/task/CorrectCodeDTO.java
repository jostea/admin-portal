package com.internship.adminpanel.model.dto.task;

import com.internship.adminpanel.model.CorrectCode;
import lombok.Data;

@Data
public class CorrectCodeDTO {
    private Long id;
    private Long codeTaskId;
    private String input;
    private String output;
    public CorrectCodeDTO(CorrectCode correctCode) {
        this.id = correctCode.getId();
        this.codeTaskId = correctCode.getCodeTask().getId();
        this.input = correctCode.getInput();
        this.output = correctCode.getOutput();
    }
}
