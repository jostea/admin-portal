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
@Table(name = "answer_option_table")
public class AnswersOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Answer text is required")
    @Column(name = "answer_option_value")
    private String answerOptionValue;

    @Column(name = "is_correct")
    boolean isCorrect;

    @ManyToOne
    private Task task;
}
