package com.internship.adminpanel.model.dto.test_report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SkillGroupAnswerDTO {

    private String skillType;
    private List<SkillCandidateDTO> skillCandidateList;

    public SkillGroupAnswerDTO(){
        this.setSkillCandidateList(null);
    }

}
