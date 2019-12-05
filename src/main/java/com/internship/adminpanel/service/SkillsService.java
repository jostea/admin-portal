package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.exception.SkillNotFound;
import com.internship.adminpanel.model.Skill;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.skill.SkillDTO;
import com.internship.adminpanel.model.dto.skill.SkillDTOFromUI;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.enums.SkillsTypeEnum;
import com.internship.adminpanel.repository.SkillsRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillsService {

    private final SkillsRepository skillsRepository;
    private final StreamRepository streamRepository;

    public SkillDTO findById(Long id) throws SkillNotFound {
        Optional<Skill> skill = skillsRepository.findById(id);
        if (skill.isPresent()) {
            return new SkillDTO(skill.get());
        } else {
            throw new SkillNotFound(id);
        }
    }

    public List<SkillDTO> findAll() throws SkillNotFound {
        List<SkillDTO> skillDTOS = new ArrayList<>();
        for (Skill val : skillsRepository.findAll()) {
            skillDTOS.add(new SkillDTO(val));
        }
        if (skillDTOS.size() == 0) {
            throw new SkillNotFound();
        }
        return skillDTOS;
    }

    public void add(SkillDTOFromUI skillDTOFromUI) throws EmptyName {
        if (skillDTOFromUI.getName().isEmpty())
            throw new EmptyName();
        skillsRepository.save(Skill.builder()
                .name(skillDTOFromUI.getName())
                .skillType(SkillsTypeEnum.fromString(skillDTOFromUI.getTypeStr()))
                .streams(getStreamsByIds(skillDTOFromUI))
                .build());
    }

    public void update(Long id, SkillDTOFromUI skillDTOFromUI) throws SkillNotFound, EmptyName {
        Optional<Skill> skillOptional = skillsRepository.findById(id);
        if (skillOptional.isPresent()) {
            Skill skill = skillOptional.get();
            if (skillDTOFromUI.getName().isEmpty())
                throw new EmptyName();
            skill.setName(skillDTOFromUI.getName());
            skill.setSkillType(SkillsTypeEnum.fromString(skillDTOFromUI.getTypeStr()));
            skill.setStreams(getStreamsByIds(skillDTOFromUI));
            skillsRepository.save(skill);
        } else {
            throw new SkillNotFound(id);
        }
    }

    public void delete(Long id) throws SkillNotFound {
        if (skillsRepository.findById(id).isPresent())
            skillsRepository.deleteById(id);
        else
            throw new SkillNotFound(id);
    }

    private List<Stream> getStreamsByIds(SkillDTOFromUI skillDTOFromUI) {
        List<Stream> streams = new ArrayList<>();
        for (Long val : skillDTOFromUI.getStreamsId()) {
            Optional<Stream> streamOptional = streamRepository.findById(val);
            if (streamOptional.isPresent())
                streams.add(streamOptional.get());
        }
        return streams;
    }
}
