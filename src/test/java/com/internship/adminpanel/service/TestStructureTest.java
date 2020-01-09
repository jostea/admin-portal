package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.exception.TestStructureNotFound;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.TestStructure;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTO;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTOFromUI;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TaskTypeEnum;
import com.internship.adminpanel.repository.StreamRepository;
import com.internship.adminpanel.repository.TestStructureRepository;
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
public class TestStructureTest {

    @Mock
    private TestStructureRepository testStructureRepository;

    @Mock
    private StreamRepository streamRepository;

    @InjectMocks
    private TestStructureService testStructureService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldFindTestStructure() throws TestStructureNotFound {
        TestStructure testStructure = createTestStructure();
        TestStructureDTO testStructureDTO = createTestStructureDTO();
        when(testStructureRepository.findById(1L)).thenReturn(Optional.of(testStructure));
        TestStructureDTO returnedResult = testStructureService.findById(1L);
        verify(testStructureRepository, times(2)).findById(1L);
        assertThat(returnedResult).isEqualTo(testStructureDTO);
    }

    @Test
    public void shouldReturnedAll(){
        List<TestStructureDTO> testStructureDTOS = new ArrayList<>();
        testStructureDTOS.add(createTestStructureDTO());
        List<TestStructure> testStructures = new ArrayList<>();
        testStructures.add(createTestStructure());
        when(testStructureRepository.findAll()).thenReturn(testStructures);
        List<TestStructureDTO> returnedList = testStructureService.findAll();
        verify(testStructureRepository).findAll();
        assertThat(returnedList).isEqualTo(testStructureDTOS);
    }

    @Test
    public void shouldAddTestStructure() throws StreamNotFound, IllegalArgumentException {
        TestStructureDTOFromUI testStructureDTOFromUI = createTestStructureFromUI();
        TestStructure testStructure = createTestStructureByUi(testStructureDTOFromUI);
        when(streamRepository.findById(1L)).thenReturn(Optional.of(createStream()));
        testStructureService.add(testStructureDTOFromUI);
        verify(streamRepository).findById(1L);
        verify(testStructureRepository).save(testStructure);
    }

    @Test
    public void shouldUpdateTestStructure() throws StreamNotFound, TestStructureNotFound, IllegalArgumentException {
        Long id = 1L;
        when(testStructureRepository.findById(id)).thenReturn(Optional.of(createTestStructure()));
        when(streamRepository.findById(id)).thenReturn(Optional.of(createStream()));
        testStructureService.update(id, createTestStructureFromUI());
        verify(testStructureRepository).findById(id);
        verify(streamRepository).findById(id);
        verify(testStructureRepository).save(createTestStructure());
    }

    @Test
    public void shouldDeleteTestStructure() throws TestStructureNotFound {
        Long id = 1L;
        when(testStructureRepository.findById(id)).thenReturn(Optional.of(createTestStructure()));
        testStructureService.delete(id);
        verify(testStructureRepository).findById(id);
        verify(testStructureRepository).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionTestStructureNotFoundWhenFindTestById() throws TestStructureNotFound {
        Long id = 1L;
        exception.expect(TestStructureNotFound.class);
        exception.expectMessage(id + "");
        when(testStructureRepository.findById(id)).thenReturn(Optional.empty());
        testStructureService.findById(id);
        verify(testStructureRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionTestStructureNotFoundWhenUpdateTestStructure() throws StreamNotFound, TestStructureNotFound, IllegalArgumentException {
        Long id = 1L;
        exception.expect(TestStructureNotFound.class);
        exception.expectMessage(id + "");
        when(testStructureRepository.findById(id)).thenReturn(Optional.empty());
        testStructureService.update(id, createTestStructureFromUI());
        verify(testStructureRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhenEditTest() throws StreamNotFound, TestStructureNotFound, IllegalArgumentException {
        exception.expect(StreamNotFound.class);
        exception.expectMessage("");
        Long id = 1L;
        when(testStructureRepository.findById(id)).thenReturn(Optional.of(createTestStructure()));
        when(streamRepository.findById(id)).thenReturn(Optional.empty());
        testStructureService.update(id, createTestStructureFromUI());
        verify(testStructureRepository).findById(id);
        verify(streamRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionTestStructureNotFoundWhenDeleteById() throws TestStructureNotFound {
        exception.expectMessage("");
        exception.expect(TestStructureNotFound.class);
        when(testStructureRepository.findById(1L)).thenReturn(Optional.empty());
        testStructureService.delete(1L);
        verify(testStructureRepository).deleteById(1L);
    }

    private TestStructureDTO createTestStructureDTO() {
        return TestStructureDTO.builder()
                .complexity("Easy")
                .taskType("Single Choice Question")
                .nrQuestions(9L)
                .id(1L)
                .build();
    }

    private TestStructure createTestStructure() {
        return TestStructure.builder()
                .complexity(ComplexityEnum.EASY)
                .nrQuestions(9L)
                .taskType(TaskTypeEnum.SINGLE_CHOICE)
                .id(1L)
                .stream(createStream())
                .build();
    }

    private TestStructure createTestStructureByUi(TestStructureDTOFromUI testStructureDTOFromUI) {
        return TestStructure.builder()
                .complexity(ComplexityEnum.fromString(testStructureDTOFromUI.getComplexity()))
                .taskType(TaskTypeEnum.fromString(testStructureDTOFromUI.getTaskType()))
                .nrQuestions(testStructureDTOFromUI.getNrQuestions())
                .stream(createStream())
                .build();
    }

    private TestStructureDTOFromUI createTestStructureFromUI() {
        return TestStructureDTOFromUI.builder()
                .complexity("Easy")
                .nrQuestions(9L)
                .streamId(1L)
                .taskType("Single Choice Question")
                .build();
    }

    private Stream createStream() {
        return Stream.builder()
                .id(1L)
                .name("TestStream")
                .discipline(Discipline.builder().name("TestDiscipline").build())
                .build();
    }
}
