package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.exception.SkillNotFound;
import com.internship.adminpanel.model.Skill;
import com.internship.adminpanel.model.dto.skill.SkillDTO;
import com.internship.adminpanel.model.dto.skill.SkillDTOFromUI;
import com.internship.adminpanel.service.SkillsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SkillsRestControllerTest {

    @Mock
    private SkillsService skillsService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SkillsRestController skillsRestController;

    @Test
    public void shouldFindSkillSpecifiedById() throws SkillNotFound {
        Long id = 1L;
        SkillDTO skillDTO = createSkillDTO("Kaban");
        when(skillsService.findById(id)).thenReturn(skillDTO);
        ResponseEntity<SkillDTO> responseEntity = skillsRestController.findById(id, authentication);
        verify(skillsService).findById(id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(skillDTO);
    }

    @Test
    public void shouldGetAllSkills() throws SkillNotFound {
        List<SkillDTO> skillDTOS = new ArrayList<>();
        skillDTOS.add(createSkillDTO("Myxtap"));
        skillDTOS.add(createSkillDTO("TestName"));
        when(skillsService.findAll()).thenReturn(skillDTOS);
        ResponseEntity<List<SkillDTO>> responseEntity = skillsRestController.findAll(authentication);
        verify(skillsService).findAll();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldAddSkill() throws EmptyName {
        SkillDTOFromUI skillDTOFromUI = createSkillDtoFromUI("Boris");
        ResponseEntity<String> responseEntity = skillsRestController.add(skillDTOFromUI, authentication);
        verify(skillsService).add(skillDTOFromUI);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldUpdateSkill() throws SkillNotFound, EmptyName {
        Long id = 1L;
        SkillDTOFromUI skillDTOFromUI = createSkillDtoFromUI("Boris");
        ResponseEntity<String> responseEntity = skillsRestController.update(id, skillDTOFromUI, authentication);
        verify(skillsService).update(id, skillDTOFromUI);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteSkillById() throws SkillNotFound {
        Long id = 1L;
        ResponseEntity<String> responseEntity = skillsRestController.delete(id, authentication);
        verify(skillsService).delete(id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileFindSkillById() throws SkillNotFound {
        doThrow(SkillNotFound.class).when(skillsService).findById(1L);
        ResponseEntity<SkillDTO> responseEntity = skillsRestController.findById(1L, authentication);
        verify(skillsService).findById(1L);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionEmptyNameWhileAddSkill() throws EmptyName {
        doThrow(EmptyName.class).when(skillsService).add(createSkillDtoFromUI(""));
        ResponseEntity<String> responseEntity = skillsRestController.add(createSkillDtoFromUI(""), authentication);
        verify(skillsService).add(createSkillDtoFromUI(""));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldThrowExceptionAlreadyExistWhileAddSkill() throws EmptyName {
        doThrow(DataIntegrityViolationException.class).when(skillsService).add(createSkillDtoFromUI("TestName"));
        ResponseEntity<String> responseEntity = skillsRestController.add(createSkillDtoFromUI("TestName"), authentication);
        verify(skillsService).add(createSkillDtoFromUI("TestName"));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Skill '" + createSkillDtoFromUI("TestName").getName() + "' already exist");
    }

    @Test
    public void shouldThrowExceptionEmptyNameWhileEditSkill() throws EmptyName, SkillNotFound {
        doThrow(EmptyName.class).when(skillsService).update(1L, createSkillDtoFromUI(""));
        ResponseEntity<String> responseEntity = skillsRestController.update(1L, createSkillDtoFromUI(""), authentication);
        verify(skillsService).update(1L, createSkillDtoFromUI(""));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileEditSkill() throws EmptyName, SkillNotFound {
        doThrow(SkillNotFound.class).when(skillsService).update(1L, createSkillDtoFromUI("TestName"));
        ResponseEntity<String> responseEntity = skillsRestController.update(1L, createSkillDtoFromUI("TestName"), authentication);
        verify(skillsService).update(1L, createSkillDtoFromUI("TestName"));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionSkillNotFoundWhileDeleteSkill() throws EmptyName, SkillNotFound {
        doThrow(SkillNotFound.class).when(skillsService).delete(1L);
        ResponseEntity<String> responseEntity = skillsRestController.delete(1L, authentication);
        verify(skillsService).delete(1L);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private SkillDTOFromUI createSkillDtoFromUI(String name) {
        return SkillDTOFromUI.builder()
                .name(name)
                .build();
    }

    private Skill createSkill(String name) {
        return Skill.builder()
                .name(name)
                .build();
    }

    private SkillDTO createSkillDTO(String name) {
        return SkillDTO.builder()
                .name(name)
                .build();
    }
}
