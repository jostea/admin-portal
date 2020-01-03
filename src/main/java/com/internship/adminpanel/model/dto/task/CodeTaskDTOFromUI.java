package com.internship.adminpanel.model.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.CorrectCode;
import com.internship.adminpanel.model.dto.code_task.AnswersSubmitDTO;
import com.internship.adminpanel.model.dto.code_task.CodeTaskSubmitDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TechnologyEnum;
import com.internship.adminpanel.service.CodeValidationService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CodeTaskDTOFromUI {
    private String title;
    private String description;
    private String signature;
    private TechnologyEnum technology;
    private ComplexityEnum complexity;
    @JsonProperty
    private boolean isEnabled;
    private List<CorrectCode> correctCodes;
    private List<Long> streams;

    public CodeTaskDTOFromUI(CodeTaskSubmitDTO codeTaskSubmitDTO) {
        CodeValidationService codeValidationService = new CodeValidationService();
        this.setTitle(codeTaskSubmitDTO.getTitle());
        this.setComplexity(codeTaskSubmitDTO.getComplexity());
        this.setTechnology(codeTaskSubmitDTO.getTechnology());
        List<CorrectCode> currentCodes = new ArrayList<>();
        for (AnswersSubmitDTO answer : codeTaskSubmitDTO.getAnswersSubmit()) {
            CorrectCode inLoopCorrectCode = new CorrectCode(answer);
            currentCodes.add(inLoopCorrectCode);
        }
        this.setCorrectCodes(currentCodes);
        this.setDescription(codeTaskSubmitDTO.getDescription());
        this.setSignature(codeValidationService.cleanSignature(codeTaskSubmitDTO.getSignature()));
        this.setEnabled(codeTaskSubmitDTO.isEnabled());
    }

    @Override
    public String toString() {
        return "CodeTaskDTOFromUI{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
