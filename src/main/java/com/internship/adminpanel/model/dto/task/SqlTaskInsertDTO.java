package com.internship.adminpanel.model.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SqlTaskInsertDTO {

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "Complexity is required")
    @Enumerated(EnumType.STRING)
    private ComplexityEnum complexity;

    @JsonProperty
    private boolean isEnabled;

    @NotNull(message = "Correct SQL statement is required")
    private String correctStatement;

    private List<Long> streams;

    private Long sqlGroupId;

    @Override
    public String toString() {
        return "SqlTaskInsertDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", complexity=" + complexity +
                ", isEnabled=" + isEnabled +
                ", correctStatement='" + correctStatement + '\'' +
                ", streams=" + streams +
                '}';
    }
}
