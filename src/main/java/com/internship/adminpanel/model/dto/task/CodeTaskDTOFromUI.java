package com.internship.adminpanel.model.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.CorrectCode;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TechnologyEnum;
import lombok.Data;

import java.util.List;

@Data
public class CodeTaskDTOFromUI {
    private String title;
    private String description;
    private String signature;
    private TechnologyEnum technology;
    private ComplexityEnum complexity;
    @JsonProperty
    private boolean isEnabled;
    private List<CorrectCode> correctCodes;
    private List<Long> streams;
}
