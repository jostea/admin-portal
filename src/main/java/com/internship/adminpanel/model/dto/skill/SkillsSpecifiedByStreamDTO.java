package com.internship.adminpanel.model.dto.skill;

import com.internship.adminpanel.model.Skill;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillsSpecifiedByStreamDTO {
    private Long id;

    private String name;

    private String typeStr;

    public SkillsSpecifiedByStreamDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.setTypeStr(skill.getSkillType().getType());
    }
}
