package com.internship.adminpanel.model.dto.code_task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TechnologyEnum;
import lombok.Data;
import java.util.List;

@Data
public class CodeTaskSubmitDTO {
    private String title;
    private TechnologyEnum technology;
    private ComplexityEnum complexity;
    private String description;
    private String signature;
    @JsonProperty
    private boolean isEnabled;
    private List<AnswersSubmitDTO> answersSubmit;
    private List<Long> streams;
}