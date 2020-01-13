package com.internship.adminpanel.model.dto.test_report;

import com.internship.adminpanel.model.CandidateSqlTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlAnswerDTO {

    private Long idCandidateSqlTask;
    private String textQuestion;
    private String correctStatement;
    private String candidateStatement;
    private String message;
    private boolean isCorrect;

    public SqlAnswerDTO (CandidateSqlTask candSqlTask){
        this.setIdCandidateSqlTask(candSqlTask.getId());
        this.setTextQuestion(candSqlTask.getSqlTask().getDescription());
        this.setCorrectStatement(candSqlTask.getSqlTask().getCorrectStatement());
        this.setCandidateStatement(candSqlTask.getStatementProvided());
        this.setMessage(candSqlTask.getMessage());
        this.setCorrect(candSqlTask.isCorrect());
    }
}
