package com.internship.adminpanel.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamDTO {
    private Long streamId;

    private String streamName;

    public StreamDTO(Stream stream) {
        this.streamId = stream.getStreamId();
        this.streamName = stream.getStreamName();
    }
}
