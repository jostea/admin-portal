package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.exception.TestStructureNotFound;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.TestStructure;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTO;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTOFromUI;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TaskTypeEnum;
import com.internship.adminpanel.repository.StreamRepository;
import com.internship.adminpanel.repository.TestStructureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestStructureService {
    private final TestStructureRepository testStructureRepository;
    private final StreamRepository streamRepository;

    public TestStructureDTO findById(Long id) throws TestStructureNotFound {
        if (testStructureRepository.findById(id).isPresent()) {
            return new TestStructureDTO(testStructureRepository.findById(id).get());
        } else {
            throw new TestStructureNotFound(id);
        }
    }

    public List<TestStructureDTO> findAll(){
        List<TestStructureDTO> testStructureDTOS = new ArrayList<>();
        for (TestStructure ts : testStructureRepository.findAll()) {
            testStructureDTOS.add(new TestStructureDTO(ts));
        }
        return testStructureDTOS;
    }

    public void add(TestStructureDTOFromUI testUI) throws StreamNotFound, IllegalArgumentException {
        if (testUI.getNrQuestions() < 0)
            throw new IllegalArgumentException();
        testStructureRepository.save(TestStructure.builder()
                .complexity(ComplexityEnum.fromString(testUI.getComplexity()))
                .taskType(TaskTypeEnum.fromString(testUI.getTaskType()))
                .nrQuestions(testUI.getNrQuestions())
                .stream(getStream(testUI.getStreamId()))
                .build());
    }

    public void update(Long id, TestStructureDTOFromUI testUI) throws StreamNotFound, TestStructureNotFound, IllegalArgumentException {
        Optional<TestStructure> testStructureOptional = testStructureRepository.findById(id);
        TestStructure testStructure;
        if (testStructureOptional.isPresent()) {
            if (testUI.getNrQuestions() < 0)
                throw new IllegalArgumentException();
            testStructure = testStructureOptional.get();
            testStructure.setComplexity(ComplexityEnum.fromString(testUI.getComplexity()));
            testStructure.setTaskType(TaskTypeEnum.fromString(testUI.getTaskType()));
            testStructure.setNrQuestions(testUI.getNrQuestions());
            testStructure.setStream(getStream(testUI.getStreamId()));
            testStructureRepository.save(testStructure);
        } else {
            throw new TestStructureNotFound(id);
        }
    }

    public void delete(Long id) throws TestStructureNotFound {
        Optional<TestStructure> testStructure = testStructureRepository.findById(id);
        if (testStructure.isPresent()) {
            testStructureRepository.deleteById(id);
        } else {
            throw new TestStructureNotFound(id);
        }
    }

    private Stream getStream(Long id) throws StreamNotFound {
        Optional<Stream> streamOptional = streamRepository.findById(id);
        Stream stream;
        if (streamOptional.isPresent()) {
            stream = streamOptional.get();
        } else {
            throw new StreamNotFound(id + "");
        }
        return stream;
    }
}
