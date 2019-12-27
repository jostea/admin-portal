package com.internship.adminpanel.model.dto.task;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.CorrectCode;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CodeTaskDTO {
    private Long id;
    private String title;
    private String description;
    private String complexity;
    private String technology;
    private boolean isEnabled;
    private String signature;
    private List<StreamDTO> streams;
    private List<CorrectCodeDTO> correctCodes;

    public CodeTaskDTO(CodeTask codeTask) {
        this.id = codeTask.getId();
        this.title = codeTask.getTitle();
        this.description = codeTask.getDescription();
        this.signature = codeTask.getSignature();
        this.isEnabled = codeTask.isEnabled();
        this.technology = codeTask.getTechnology().getType();
        this.complexity = codeTask.getComplexity().getComplexity();
        List<StreamDTO> strm = new ArrayList<>();
        for (Stream stream:codeTask.getStreams()) {
            strm.add(new StreamDTO(stream));
        }
        List<CorrectCodeDTO> crctCds = new ArrayList<>();
        for (CorrectCode correctCode:codeTask.getCorrectCodes()) {
            crctCds.add(new CorrectCodeDTO(correctCode));
        }
        this.streams = strm;
        this.correctCodes = crctCds;
    }
}
