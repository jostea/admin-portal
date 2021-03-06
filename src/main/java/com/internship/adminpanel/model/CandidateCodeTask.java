package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate_code_task")
public class CandidateCodeTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "code_task_id")
    private CodeTask codeTask;

    @Column(name = "code_provided")
    private String codeProvided;

    @Column(name = "rate_correctness")
    private Double rateCorrectness;

    @Column(name = "message")
    private String message;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    public CandidateCodeTask(CodeTask codeTask, Candidate candidate) {
        this.candidate = candidate;
        this.codeTask = codeTask;
    }
}
