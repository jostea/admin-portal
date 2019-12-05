package com.internship.adminpanel.model.dto.skill;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SkillDTOFromUI {
    private Long id;

    private String name;

    private String typeStr;

    private List<Long> streamsId;
}
