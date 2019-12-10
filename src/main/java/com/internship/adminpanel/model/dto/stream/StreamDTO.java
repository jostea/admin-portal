package com.internship.adminpanel.model.dto.stream;

import com.internship.adminpanel.model.Skill;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.TestStructure;
import com.internship.adminpanel.model.dto.skill.SkillDTO;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamDTO {

    private Long id;

    private String name;

    private String disciplineName;

    private Long disciplineId;

    private List<TestStructureDTO> testStructures;

    private List<SkillDTO> skills;

    public StreamDTO(Stream stream) {
        this.disciplineId = stream.getDiscipline().getId();
        this.id = stream.getId();
        this.name = stream.getName();
        this.disciplineName = stream.getDiscipline().getName();
        if (stream.getTestStructures().size() != 0) {
            this.testStructures = new ArrayList<>();
            for (TestStructure ts : stream.getTestStructures()) {
                this.testStructures.add(new TestStructureDTO(ts));
            }
        }
        if (stream.getSkill().size() != 0) {
            this.skills = new ArrayList<>();
            for (Skill skill : stream.getSkill()) {
                this.skills.add(new SkillDTO(skill));
            }
        }
    }
}
