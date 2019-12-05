package com.internship.adminpanel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternshipDTO {

    private Long id;
    private Boolean isCurrent;
    private List<Long> streamIDs;

}
