package com.internship.adminpanel.model.dto.stream;

import com.internship.adminpanel.model.StreamTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamTimeDTO {

    private Long id;

    private Integer time;

    public StreamTimeDTO(StreamTime streamTime) {
        this.id = streamTime.getId();
        this.time = streamTime.getTimeTest();
    }
}
