package com.internship.adminpanel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long streamId;

    private Long candidateId;

    private String name;

    private String discipline;

    private String stream;

    private String email;
}
