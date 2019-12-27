package com.internship.adminpanel.service;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.CorrectCode;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.code_task.AnswersSubmitDTO;
import com.internship.adminpanel.model.dto.code_task.CodeTaskSubmitDTO;
import com.internship.adminpanel.model.dto.task.CodeTaskDTO;
import com.internship.adminpanel.model.dto.task.CodeTaskDTOFromUI;
import com.internship.adminpanel.repository.CodeTaskRepository;
import com.internship.adminpanel.repository.CorrectCodeRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CodeTaskService {
    private final CodeTaskRepository codeTaskRepository;
    private final StreamRepository streamRepository;
    private final CodeValidationService codeValidationService;
    private final CorrectCodeRepository correctCodeRepository;

    public void saveTask(CodeTaskSubmitDTO codeTaskSubmitDTO) throws Exception {
        try {
            if (codeValidationService.validateSignature(codeTaskSubmitDTO.getSignature())) {
                for (AnswersSubmitDTO answer : codeTaskSubmitDTO.getAnswersSubmit()) {
                    if (!codeValidationService.validateAnswers(answer)) {
                        throw new Exception("Answer tests validation not passed");
                    }
                }
                CodeTaskDTOFromUI codeTaskDTOFromUI = new CodeTaskDTOFromUI(codeTaskSubmitDTO);
                CodeTask tobeSaved = new CodeTask(codeTaskDTOFromUI);
                for (CorrectCode code : codeTaskDTOFromUI.getCorrectCodes()) {
                    code.setCodeTask(tobeSaved);
                }
                List<Stream> codeTaskStreams = new ArrayList<>();
                for (Long s : codeTaskSubmitDTO.getStreams()) {
                    codeTaskStreams.add(streamRepository.findById(s).get());
                }
                tobeSaved.setStreams(codeTaskStreams);
                codeTaskRepository.save(tobeSaved);
                for (CorrectCode code : codeTaskDTOFromUI.getCorrectCodes()) {
                    correctCodeRepository.save(code);
                }
            } else throw new Exception("Signature validation not passed");
        } catch (Exception e) {
            throw new Exception("Task could not be saved, check all the data again. \nReason: " + e.getMessage());
        }
    }
    public List<CodeTaskDTO> getAll() throws Exception {
        try {
            List<CodeTaskDTO> taskDTOS = new ArrayList<>();
            for (CodeTask codeTask:codeTaskRepository.findAll()) {
                taskDTOS.add(new CodeTaskDTO(codeTask));
            }
            return taskDTOS;
        } catch (Exception e) {
            log.warn("Could not extract tasks from db. Stack Trace: " + Arrays.toString(e.getStackTrace()));
            throw new Exception("Code tasks could not be loaded");
        }
    }
    public CodeTaskDTO findById(Long id) throws Exception {
        try {
            return new CodeTaskDTO(codeTaskRepository.findById(id).get());
        } catch (Exception e) {
            throw new Exception("Something went wrong finding the task");
        }
    }

    public void editTask(Long id, CodeTaskSubmitDTO codeTaskSubmitDTO) throws Exception {
        try {
            if (codeValidationService.validateSignature(codeTaskSubmitDTO.getSignature())) {
                for (AnswersSubmitDTO answer : codeTaskSubmitDTO.getAnswersSubmit()) {
                    if (!codeValidationService.validateAnswers(answer)) {
                        throw new Exception("Answer tests validation not passed");
                    }
                }
                CodeTask fromRepo = codeTaskRepository.findById(id).get();
                CodeTaskDTOFromUI codeTaskDTOFromUI = new CodeTaskDTOFromUI(codeTaskSubmitDTO);
                fromRepo.setTitle(codeTaskDTOFromUI.getTitle());
                fromRepo.setDescription(codeTaskDTOFromUI.getDescription());
                List<Stream> updatedStreams = new ArrayList<>();
                for (Long strid : codeTaskDTOFromUI.getStreams()) {
                    updatedStreams.add(streamRepository.findById(strid).get());
                }
                fromRepo.setStreams(updatedStreams);
                fromRepo.setComplexity(codeTaskDTOFromUI.getComplexity());
                fromRepo.setEnabled(codeTaskDTOFromUI.isEnabled());
                fromRepo.setSignature(codeValidationService.cleanSignature(codeTaskDTOFromUI.getSignature()));
                fromRepo.setTechnology(codeTaskDTOFromUI.getTechnology());
                fromRepo.setCorrectCodes(codeTaskDTOFromUI.getCorrectCodes());
                codeTaskRepository.save(fromRepo);
            } else {
                throw new Exception("Signature validation not passed");
            }
        } catch (Exception e) {
            log.warn("Code task could not be updated. Stack trace: " + Arrays.toString(e.getStackTrace()));
            throw new Exception("Could not update the task");
        }
    }
}
