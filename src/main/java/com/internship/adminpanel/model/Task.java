package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.internship.adminpanel.model.enums.TypeEnum;
import org.springframework.beans.factory.annotation.Value;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Title is required")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Description is required")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TypeEnum taskType;

    @NotNull(message = "Complexity is required")
    @ManyToOne
    @Column(name = "complx_id")
    private Complexity complexity;

    @Value("true")
    @Column(name = "isEnabled")
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "task_stream", joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;

    @OneToMany
    private List<AnswersOption> answersOptions;
}
