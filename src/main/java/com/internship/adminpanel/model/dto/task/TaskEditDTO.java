package com.internship.adminpanel.model.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.internship.adminpanel.model.AnswersOption;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.Task;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.answer_option.AnswerOptionDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TypeEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TaskEditDTO {

    @NotEmpty
    private Long id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private TypeEnum taskType;

    @NotEmpty(message = "Complexity is required")
    @Enumerated(EnumType.STRING)
    private ComplexityEnum complexity;

    @JsonProperty
    private boolean isEnabled;

    private List<StreamDTO> streams;

    @JsonProperty
    private List<AnswerOptionDTO> answers;

    //copy constructor Task -> TaskEditDTO
    public TaskEditDTO(Task entity){
        this.setId(entity.getId());
        this.setTitle(entity.getTitle());
        this.setDescription(entity.getDescription());
        this.setTaskType(entity.getTaskType());
        this.setComplexity(entity.getComplexity());
        this.setEnabled(entity.isEnabled());

        //set Collection of StreamDTO
        List<StreamDTO> listStreamsDto = new ArrayList<>();
        for (Stream stream : entity.getStreams()) {
            StreamDTO streamDto = new StreamDTO(stream);    //convert Stream entity in StreamDTO
            listStreamsDto.add(streamDto);
        }
        this.setStreams(listStreamsDto);

        //set Collection of AnswerOptionDTO
        List<AnswerOptionDTO>  listAnswersDTO = new ArrayList<>();
        for (AnswersOption answer : entity.getAnswersOptions()) {
            AnswerOptionDTO answerDto = new AnswerOptionDTO(answer);    //convert AnswerOption entity in AnswerOptionDTO
            listAnswersDTO.add(answerDto);
        }
        this.setAnswers(listAnswersDTO);
    }
}
