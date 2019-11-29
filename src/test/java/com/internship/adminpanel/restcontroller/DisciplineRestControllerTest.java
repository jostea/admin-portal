package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.model.dto.discipline.DisciplineDTO;
import com.internship.adminpanel.service.DisciplineService;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DisciplineRestControllerTest {

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DisciplineRestController disciplineRestController;

    @Test
    public void shouldReturnedAllDisciplines() throws Exception {
        List<DisciplineDTO> disciplineDTOS = new ArrayList<>();
        disciplineDTOS.add(createDisciplineDTO());
        disciplineDTOS.add(createDisciplineDTO());
        List<DisciplineDTO> mockedDiscipline = new ArrayList<>();
        mockedDiscipline.add(createDisciplineDTO());
        mockedDiscipline.add(createDisciplineDTO());
        when(disciplineService.getAllDisciplines()).thenReturn(mockedDiscipline);
        ResponseEntity<List<DisciplineDTO>> responseEntity = disciplineRestController.getAll(authentication);
        verify(disciplineService).getAllDisciplines();
        assertThat(responseEntity.getBody()).isEqualTo(disciplineDTOS);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldAddDiscipline() throws Exception {
        DisciplineDTO disciplineDTO = createDisciplineDTO();
        ResponseEntity<String> responseEntity = disciplineRestController.add(disciplineDTO, authentication);
        verify(disciplineService).add(disciplineDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldUpdateDiscipline() throws DisciplineNotFound, EmptyName {
        Long id = 2L;
        DisciplineDTO disciplineDTO = createDisciplineDTO();
        ResponseEntity<String> responseEntity = disciplineRestController.edit(id, disciplineDTO, authentication);
        verify(disciplineService).edit(id, disciplineDTO, null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteDisciplineById() throws Exception {
        Long id = 1L;
        ResponseEntity<String> responseEntity = disciplineRestController.delete(id, authentication);
        verify(disciplineService).delete(id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnedDisciplinesSpecifiedByCharacter() throws DisciplineNotFound {
        List<DisciplineDTO> disciplineDTOS = new ArrayList<>();
        disciplineDTOS.add(createDisciplineDTO());
        disciplineDTOS.add(createDisciplineDTO());
        String specifiedChar = "Test";
        when(disciplineService.filterByName(specifiedChar)).thenReturn(disciplineDTOS);
        ResponseEntity<List<DisciplineDTO>> responseEntity = disciplineRestController.findByCharacters(specifiedChar, authentication);
        verify(disciplineService).filterByName(specifiedChar);
        assertThat(responseEntity.getBody()).isEqualTo(disciplineDTOS);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCatchExceptionDisciplineWhileUpdateDiscipline() throws DisciplineNotFound, EmptyName {
        Long id = 1L;
        DisciplineDTO disciplineDTO = createDisciplineDTO();
        doThrow(new DisciplineNotFound(id + "")).when(disciplineService).edit(id, disciplineDTO, null);
        ResponseEntity<String> responseEntity = disciplineRestController.edit(id, disciplineDTO, authentication);
        verify(disciplineService).edit(id, disciplineDTO, null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Discipline specified by " + id + " didn't found.");
    }

    @Test
    public void shouldCatchExceptionEmptyNameWhileAddDiscipline() throws Exception {
        DisciplineDTO disciplineDTO = createDisciplineDTO();
        doThrow(new EmptyName()).when(disciplineService).add(disciplineDTO);
        ResponseEntity<String> responseEntity = disciplineRestController.add(disciplineDTO, authentication);
        verify(disciplineService).add(disciplineDTO);
        assertThat(responseEntity.getBody()).isEqualTo("Discipline name is required");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionEmptyNameWhileUpdateDiscipline() throws Exception {
        Long id = 1L;
        DisciplineDTO disciplineDTO = createDisciplineDTO();
        doThrow(new EmptyName()).when(disciplineService).edit(id, disciplineDTO, null);
        ResponseEntity<String> responseEntity = disciplineRestController.edit(id, disciplineDTO, authentication);
        verify(disciplineService).edit(id, disciplineDTO, null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionDisciplineNotFoundWhileFindDisciplineByCharacter() throws DisciplineNotFound {
        String name = "Bandit";
        doThrow(new DisciplineNotFound(name)).when(disciplineService).filterByName(name);
        ResponseEntity<List<DisciplineDTO>> responseEntity = disciplineRestController.findByCharacters(name, authentication);
        verify(disciplineService).filterByName(name);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCatchExceptionWhileAddDiscipline() throws Exception {
        DisciplineDTO disciplineDTO = createDisciplineDTO();
        doThrow(new Exception()).when(disciplineService).add(disciplineDTO);
        ResponseEntity<String> responseEntity = disciplineRestController.add(disciplineDTO, authentication);
        verify(disciplineService).add(disciplineDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionWhileDeletedById() throws Exception {
        Long id = 1L;
        doThrow(new Exception()).when(disciplineService).delete(id);
        ResponseEntity<String> responseEntity = disciplineRestController.delete(id, authentication);
        verify(disciplineService).delete(id);
        assertThat(responseEntity.getBody()).isEqualTo("Discipline has streams");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionWhileGetAllDisciplines() throws Exception {
        when(disciplineService.getAllDisciplines()).thenThrow(new Exception());
        ResponseEntity<List<DisciplineDTO>> responseEntity = disciplineRestController.getAll(authentication);
        verify(disciplineService).getAllDisciplines();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    private DisciplineDTO createDisciplineDTO() {
        return DisciplineDTO.builder()
                .id(1L)
                .name("TestDiscipline")
                .build();
    }
}
