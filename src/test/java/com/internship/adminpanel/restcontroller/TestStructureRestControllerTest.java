package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.exception.TestStructureNotFound;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTO;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTOFromUI;
import com.internship.adminpanel.service.TestStructureService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestStructureRestControllerTest {

    @Mock
    private TestStructureService testStructureService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TestStructureRestController testStructureRestController;

    @Test
    public void shouldReturnedTestSpecifiedById() throws TestStructureNotFound {
        Long id = 1L;
        when(testStructureService.findById(id)).thenReturn(createTestStructureDTO());
        ResponseEntity<TestStructureDTO> responseEntity = testStructureRestController.findById(id, authentication);
        verify(testStructureService).findById(id);
        assertThat(responseEntity.getBody()).isEqualTo(createTestStructureDTO());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnedAllTests() throws TestStructureNotFound {
        List<TestStructureDTO> testStructureDTOList = new ArrayList<>();
        testStructureDTOList.add(createTestStructureDTO());
        when(testStructureService.findAll()).thenReturn(testStructureDTOList);
        ResponseEntity<List<TestStructureDTO>> responseEntity = testStructureRestController.findAll(authentication);
        verify(testStructureService).findAll();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(testStructureDTOList);
    }

    @Test
    public void shouldAddTestStructure() throws StreamNotFound {
        ResponseEntity<String> responseEntity = testStructureRestController.add(createTestStructureFromUI(), authentication);
        verify(testStructureService).add(createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTestStructure() throws StreamNotFound, TestStructureNotFound {
        Long id = 1L;
        ResponseEntity<String> responseEntity = testStructureRestController.update(id, createTestStructureFromUI(), authentication);
        verify(testStructureService).update(id, createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteTestStructure() throws TestStructureNotFound {
        Long id = 1L;
        ResponseEntity<String> responseEntity = testStructureRestController.delete(id, authentication);
        verify(testStructureService).delete(id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCatchExceptionTestStructureNotFoundWhenFindById() throws TestStructureNotFound {
        doThrow(new TestStructureNotFound()).when(testStructureService).findById(1L);
        ResponseEntity<TestStructureDTO> responseEntity = testStructureRestController.findById(1L, authentication);
        verify(testStructureService).findById(1L);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCatchExceptionTestStructureNotFoundWhenFindAll() throws TestStructureNotFound {
        doThrow(new TestStructureNotFound()).when(testStructureService).findAll();
        ResponseEntity<List<TestStructureDTO>> responseEntity = testStructureRestController.findAll(authentication);
        verify(testStructureService).findAll();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhenAddTest() throws StreamNotFound {
        doThrow(new StreamNotFound("")).when(testStructureService).add(createTestStructureFromUI());
        ResponseEntity<String> responseEntity = testStructureRestController.add(createTestStructureFromUI(), authentication);
        verify(testStructureService).add(createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionAlreadyExistWhenAddTest() throws DataIntegrityViolationException, StreamNotFound {
        doThrow(new DataIntegrityViolationException("")).when(testStructureService).add(createTestStructureFromUI());
        ResponseEntity<String> responseEntity = testStructureRestController.add(createTestStructureFromUI(), authentication);
        verify(testStructureService).add(createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Test structure already exist");
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhenEditTest() throws StreamNotFound, TestStructureNotFound {
        doThrow(new StreamNotFound("")).when(testStructureService).update(1L, createTestStructureFromUI());
        ResponseEntity<String> responseEntity = testStructureRestController.update(1L, createTestStructureFromUI(), authentication);
        verify(testStructureService).update(1L, createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionTestStructureNotFoundWhenEditTest() throws StreamNotFound, TestStructureNotFound {
        doThrow(new TestStructureNotFound(1L)).when(testStructureService).update(1L, createTestStructureFromUI());
        ResponseEntity<String> responseEntity = testStructureRestController.update(1L, createTestStructureFromUI(), authentication);
        verify(testStructureService).update(1L, createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionAlreadyExistWhenEditTest() throws DataIntegrityViolationException, StreamNotFound, TestStructureNotFound {
        doThrow(new DataIntegrityViolationException("")).when(testStructureService).update(1L, createTestStructureFromUI());
        ResponseEntity<String> responseEntity = testStructureRestController.update(1L, createTestStructureFromUI(), authentication);
        verify(testStructureService).update(1L, createTestStructureFromUI());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Test structure already exist");
    }

    @Test
    public void shouldThrowExceptionTestStructureNotFoundWhenDeleteTest() throws TestStructureNotFound {
        doThrow(new TestStructureNotFound(1L)).when(testStructureService).delete(1L);
        ResponseEntity<String> responseEntity = testStructureRestController.delete(1L, authentication);
        verify(testStructureService).delete(1L);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private TestStructureDTOFromUI createTestStructureFromUI() {
        return TestStructureDTOFromUI.builder()
                .complexity("Easy")
                .nrQuestions(9L)
                .streamId(1L)
                .taskType("Single Choice Question")
                .build();
    }

    private TestStructureDTO createTestStructureDTO() {
        return TestStructureDTO.builder()
                .complexity("Easy")
                .taskType("Single Choice Question")
                .nrQuestions(9L)
                .id(1L)
                .build();
    }
}
