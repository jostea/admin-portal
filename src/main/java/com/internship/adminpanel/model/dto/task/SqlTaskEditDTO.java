package com.internship.adminpanel.model.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.SqlTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SqlTaskEditDTO {

    @NotEmpty
    private Long id;

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

    private List<StreamDTO> streams;

    private SqlGroupDTO sqlGroupDTO;

    public SqlTaskEditDTO(SqlTask sqlTask){

        this.setId(sqlTask.getId());
        this.setTitle(sqlTask.getTitle());
        this.setDescription(sqlTask.getDescription());
        this.setComplexity(sqlTask.getComplexity());
        this.setEnabled(sqlTask.isEnabled());
        this.setCorrectStatement(sqlTask.getCorrectStatement());

        //set Collection of StreamDTO
        List<StreamDTO> listStreamsDto = new ArrayList<>();
        for (Stream stream : sqlTask.getStreams()) {
            StreamDTO streamDto = new StreamDTO(stream);    //convert Stream entity in StreamDTO
            listStreamsDto.add(streamDto);
        }
        this.setStreams(listStreamsDto);

        //set SqlGroupDTO
        this.setSqlGroupDTO(new SqlGroupDTO(sqlTask.getSqlGroup()));
    }
}
