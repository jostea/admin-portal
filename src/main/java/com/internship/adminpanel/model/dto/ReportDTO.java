package com.internship.adminpanel.model.dto;

import com.internship.adminpanel.model.Candidate;
import com.internship.adminpanel.model.Stream;
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

    public ReportDTO(Stream stream, Candidate candidate) {
        this.streamId = stream.getId();
        this.candidateId = candidate.getId();
        this.name = candidate.getFirstName() + " " + candidate.getLastName();
        this.discipline = stream.getDiscipline().getName();
        this.stream = stream.getName();
        this.email = candidate.getEmail();
    }
}
