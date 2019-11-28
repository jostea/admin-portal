package com.internship.adminpanel.model.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SqlTaskDisableDTO {

    @NotEmpty
    private Long id;

    @JsonProperty
    private boolean isEnabled;
}
