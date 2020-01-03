package com.internship.adminpanel.model.dto.skill;

import com.internship.adminpanel.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillsStreamDTO {
    private Long id;

    private String name;

    private String typeStr;

    public SkillsStreamDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.setTypeStr(skill.getSkillType().getType());
    }
}
