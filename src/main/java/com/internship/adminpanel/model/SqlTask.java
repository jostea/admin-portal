package com.internship.adminpanel.model;

import com.internship.adminpanel.model.dto.task.SqlTaskEditDTO;
import com.internship.adminpanel.model.dto.task.SqlTaskInsertDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sql_task_table")
public class SqlTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Complexity is required")
    @Enumerated(EnumType.STRING)
    private ComplexityEnum complexity;

    @Value("true")
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @NotNull(message = "Correct SQL statement is required")
    @Column(name = "correct_statement")
    private String correctStatement;

    @ManyToMany
    @JoinTable(name = "sql_stream_table", joinColumns = @JoinColumn(name = "sql_task_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;

    @ManyToOne
    private SqlGroup sqlGroup;

    //copy constructor from SqlInsertDTO
    public SqlTask(SqlTaskInsertDTO sqlTaskInsertDTO) {
        this.setTitle(sqlTaskInsertDTO.getTitle());
        this.setDescription(sqlTaskInsertDTO.getDescription());
        this.setComplexity(sqlTaskInsertDTO.getComplexity());
        this.setEnabled(sqlTaskInsertDTO.isEnabled());
        this.setCorrectStatement(sqlTaskInsertDTO.getCorrectStatement());
    }

    //copy constructor from SqlEditDTO
    public SqlTask(SqlTaskEditDTO sqlTaskEditDTO) {
        this.setId(sqlTaskEditDTO.getId());
        this.setTitle(sqlTaskEditDTO.getTitle());
        this.setDescription(sqlTaskEditDTO.getDescription());
        this.setComplexity(sqlTaskEditDTO.getComplexity());
        this.setEnabled(sqlTaskEditDTO.isEnabled());
        this.setCorrectStatement(sqlTaskEditDTO.getCorrectStatement());
    }
}
