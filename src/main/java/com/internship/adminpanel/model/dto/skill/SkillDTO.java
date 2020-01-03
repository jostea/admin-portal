package com.internship.adminpanel.model.dto.skill;

import com.internship.adminpanel.model.Skill;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamSkillDTO;
import com.internship.adminpanel.model.enums.SkillsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTO {
    private Long id;

    private String name;

    private String typeStr;

    private List<StreamSkillDTO> streams;

    public SkillDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.setTypeStr(skill.getSkillType().getType());
        streams = new ArrayList<>();
            for (Stream s : skill.getStreams()) {
                this.streams.add(new StreamSkillDTO(s));
            }
    }
}
