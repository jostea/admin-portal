package com.internship.adminpanel.model;

import com.internship.adminpanel.model.dto.task.CodeTaskDTOFromUI;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TechnologyEnum;
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
@Table(name = "code_task_table")
public class CodeTask {

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

    @NotNull(message = "Technology is required")
    @Enumerated(EnumType.STRING)
    private TechnologyEnum technology;

    @Value("true")
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @NotNull(message = "Signature of method is required")
    private String signature;

    @ManyToMany
    @JoinTable(name = "code_stream_table", joinColumns = @JoinColumn(name = "code_task_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;

    @OneToMany(mappedBy = "codeTask")
    private List<CorrectCode> correctCodes;

    public CodeTask(CodeTaskDTOFromUI codeTaskDTOFromUI) {
        this.title = codeTaskDTOFromUI.getTitle();
        this.description = codeTaskDTOFromUI.getDescription();
        this.signature = codeTaskDTOFromUI.getSignature();
        this.technology = codeTaskDTOFromUI.getTechnology();
        this.isEnabled = codeTaskDTOFromUI.isEnabled();
        this.correctCodes = codeTaskDTOFromUI.getCorrectCodes();
        this.complexity = codeTaskDTOFromUI.getComplexity();
    }

    @Override
    public String toString() {
        return "CodeTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
