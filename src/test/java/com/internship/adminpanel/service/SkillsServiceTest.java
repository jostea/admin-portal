package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.exception.SkillNotFound;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.Skill;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.skill.SkillDTO;
import com.internship.adminpanel.model.dto.skill.SkillDTOFromUI;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.enums.SkillsTypeEnum;
import com.internship.adminpanel.repository.SkillsRepository;
import com.internship.adminpanel.repository.StreamRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SkillsServiceTest {

    @Mock
    private SkillsRepository skillsRepository;

    @Mock
    private StreamRepository streamRepository;

    @InjectMocks
    private SkillsService skillsService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldFindAllSkills() throws SkillNotFound {
        List<SkillDTO> expectedResult = new ArrayList<>();
        expectedResult.add(createSkillDTO("TestSkill1"));
        expectedResult.add(createSkillDTO("TestSkill2"));
        List<Skill> mockedSkill = new ArrayList<>();
        mockedSkill.add(createSkillSpecifiedByName("TestSkill1"));
        mockedSkill.add(createSkillSpecifiedByName("TestSkill2"));
        when(skillsRepository.findAll()).thenReturn(mockedSkill);
        List<SkillDTO> returnedList = skillsService.findAll();
        verify(skillsRepository).findAll();
        assertThat(returnedList).isEqualTo(expectedResult);
    }

    @Test
    public void shouldFindSkillSpecifiedById() throws SkillNotFound {
        Long id = 1L;
        SkillDTO expectedSkill = createSkillDTO("TestSkill");
        Skill mockedSkill = createSkillSpecifiedByName("TestSkill");
        when(skillsRepository.findById(id)).thenReturn(Optional.of(mockedSkill));
        SkillDTO returnedSkill = skillsService.findById(id);
        verify(skillsRepository).findById(id);
        assertThat(returnedSkill).isEqualTo(expectedSkill);
    }

    @Test
    public void shouldAddSkill() throws EmptyName {
        SkillDTOFromUI skillDTOFromUI = createSkillDTOFromUI("TestSkill");
        Skill skill = createSkillFromSkillDTOFromUI(skillDTOFromUI);
        when(streamRepository.findById(1L)).thenReturn(Optional.of(createStreamList().get(0)));
        when(streamRepository.findById(2L)).thenReturn(Optional.of(createStreamList().get(1)));
        skillsService.add(skillDTOFromUI);
        verify(streamRepository).findById(1L);
        verify(streamRepository).findById(2L);
        verify(skillsRepository).save(skill);
    }

    @Test
    public void shouldUpdateSkill() throws SkillNotFound, EmptyName {
        Long id = 1L;
        SkillDTOFromUI skillDTOFromUI = createSkillDTOFromUI("TestSkill");
        Skill skill = createSkillFromSkillDTOFromUI(skillDTOFromUI);
        skill.setId(id);
        when(skillsRepository.findById(id)).thenReturn(Optional.of(skill));
        when(streamRepository.findById(1L)).thenReturn(Optional.of(createStreamList().get(0)));
        when(streamRepository.findById(2L)).thenReturn(Optional.of(createStreamList().get(1)));
        skillsService.update(id, skillDTOFromUI);
        verify(streamRepository).findById(1L);
        verify(streamRepository).findById(2L);
        verify(skillsRepository).save(skill);
    }

    @Test
    public void shouldDeletedSkill() throws SkillNotFound {
        Long id = 1L;
        when(skillsRepository.findById(id)).thenReturn(Optional.of(createSkillSpecifiedByName("Name")));
        skillsService.delete(id);
        verify(skillsRepository).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileFindById() throws SkillNotFound {
        Long id = 1L;
        exception.expect(SkillNotFound.class);
        exception.expectMessage("Skill with " + id + " id didn't found");
        when(skillsRepository.findById(id)).thenReturn(Optional.empty());
        skillsService.findById(id);
        verify(skillsRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileGetAll() throws SkillNotFound {
        exception.expect(SkillNotFound.class);
        exception.expectMessage("");
        List<Skill> skills = new ArrayList<>();
        when(skillsRepository.findAll()).thenReturn(skills);
        skillsService.findAll();
        verify(skillsRepository).findAll();
    }

    @Test
    public void shouldThrowExceptionEmptyNameWhileAddSkill() throws EmptyName {
        exception.expect(EmptyName.class);
        exception.expectMessage("");
        skillsService.add(SkillDTOFromUI.builder()
                .name("").build());
    }

    @Test
    public void shouldThrowExceptionEmptyNameWhileEditSkill() throws SkillNotFound, EmptyName {
        exception.expect(EmptyName.class);
        exception.expectMessage("");
        when(skillsRepository.findById(1L)).thenReturn(Optional.of(new Skill()));
        skillsService.update(1l, SkillDTOFromUI.builder()
                .name("")
                .build());
        verify(skillsRepository).findById(1L);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileEditSkill() throws SkillNotFound, EmptyName {
        exception.expect(SkillNotFound.class);
        exception.expectMessage("");
        when(skillsRepository.findById(1L)).thenReturn(Optional.empty());
        skillsService.update(1L, SkillDTOFromUI.builder()
                .name("")
                .build());
        verify(skillsRepository).findById(1L);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileDeleteSkill() throws SkillNotFound {
        exception.expectMessage("");
        exception.expect(SkillNotFound.class);
        when(skillsRepository.findById(1L)).thenReturn(Optional.empty());
        skillsService.delete(1L);
        verify(skillsRepository).findById(1L);
    }

    private Skill createSkillSpecifiedByName(String name) {
        return Skill.builder()
                .id(1L)
                .name(name)
                .skillType(SkillsTypeEnum.SOFT)
                .streams(createStreamList())
                .build();
    }

    private SkillDTO createSkillDTO(String name) {
        return SkillDTO.builder()
                .id(1L)
                .name(name)
                .typeStr("Soft")
                .build();
    }

    private SkillDTOFromUI createSkillDTOFromUI(String name) {
        List<Long> streamsIds = new ArrayList<>();
        streamsIds.add(1L);
        streamsIds.add(2L);
        return SkillDTOFromUI.builder()
                .id(1L)
                .name(name)
                .typeStr("Soft")
                .streamsId(streamsIds)
                .build();
    }

    private List<Stream> createStreamList() {
        List<Stream> streams = new ArrayList<>();
        streams.add(Stream.builder()
                .id(1L)
                .name("TestStream1")
                .discipline(Discipline.builder()
                        .id(1L)
                        .name("TestDiscipline1")
                        .build())
                .build());
        streams.add(Stream.builder()
                .id(2L)
                .name("TestStream2")
                .discipline(Discipline.builder()
                        .id(2L)
                        .name("TestDiscipline2")
                        .build())
                .build());
        return streams;
    }

    private List<StreamDTO> createStreamDTOList() {
        List<StreamDTO> streams = new ArrayList<>();
        streams.add(StreamDTO.builder()
                .id(1L)
                .name("TestStream1")
                .disciplineId(1L)
                .disciplineName("TestDiscipline1")
                .build());
        streams.add(StreamDTO.builder()
                .id(2L)
                .name("TestStream2")
                .disciplineId(2L)
                .disciplineName("TestDiscipline2")
                .build());
        return streams;
    }

    private Skill createSkillFromSkillDTOFromUI(SkillDTOFromUI SkillDTOFromUI) {
        return Skill.builder()
                .name(SkillDTOFromUI.getName())
                .streams(createStreamList())
                .skillType(SkillsTypeEnum.fromString(SkillDTOFromUI.getTypeStr()))
                .build();
    }
}
