package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "answer_option")
public class AnswersOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ao_id")
    private Long id;

    @ManyToOne
    @Column(name = "task_id")
    private Task task;

    @NotNull(message = "Answer is required")
    @Column(name = "answer_option_value")
    private String answerOptionValue;

    @Column(name = "isCorrect")
    boolean isCorrect;
}
