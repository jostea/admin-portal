package com.internship.adminpanel.model;

import com.internship.adminpanel.model.enums.SkillsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skills_table")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Skill is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "This name cannot be applied. Must contains only upper case,lower case and numbers.")
    private String name;

    @NotNull(message = "Skill type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type")
    private SkillsTypeEnum skillType;

    @ManyToMany
    @JoinTable(name = "skills_stream_table", joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;
}
