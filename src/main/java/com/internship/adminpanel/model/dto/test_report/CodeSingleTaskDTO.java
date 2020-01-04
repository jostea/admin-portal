package com.internship.adminpanel.model.dto.test_report;

import com.internship.adminpanel.model.CandidateCodeTask;
import lombok.Data;

@Data
public class CodeSingleTaskDTO {
    private Long id;
    private Long candidateId;
    private Long codeTaskId;
    private String codeProvided;
    private Double rateCorrectness;
    private String message;
    private Boolean isCorrect;

    public CodeSingleTaskDTO(CandidateCodeTask candidateCodeTask) {
        this.id = candidateCodeTask.getId();
        this.candidateId = candidateCodeTask.getCandidate().getId();
        this.codeTaskId = candidateCodeTask.getCodeTask().getId();
        this.codeProvided = candidateCodeTask.getCodeProvided();
        this.rateCorrectness = candidateCodeTask.getRateCorrectness();
        this.message = candidateCodeTask.getMessage();
        this.isCorrect = candidateCodeTask.getIsCorrect();
    }
}
