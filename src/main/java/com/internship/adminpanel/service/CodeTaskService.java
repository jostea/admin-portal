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
import java.util.Optional;

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
        Optional<CodeTask> fromRepo = codeTaskRepository.findById(id);
        try {
            if (fromRepo.isPresent()) {
                if (codeValidationService.validateSignature(codeTaskSubmitDTO.getSignature())) {
                    for (AnswersSubmitDTO answer : codeTaskSubmitDTO.getAnswersSubmit()) {
                        if (!codeValidationService.validateAnswers(answer)) {
                            throw new Exception("Answer tests validation not passed");
                        }
                    }
                    CodeTask edited = fromRepo.get();
                    CodeTaskDTOFromUI codeTaskDTOFromUI = new CodeTaskDTOFromUI(codeTaskSubmitDTO);
                    edited.setTitle(codeTaskDTOFromUI.getTitle());
                    edited.setDescription(codeTaskDTOFromUI.getDescription());
                    List<Stream> codeTaskStreams = new ArrayList<>();
                    for (Long s : codeTaskSubmitDTO.getStreams()) {
                        codeTaskStreams.add(streamRepository.findById(s).get());
                    }
                    edited.setStreams(codeTaskStreams);
                    edited.setComplexity(codeTaskDTOFromUI.getComplexity());
                    edited.setEnabled(codeTaskDTOFromUI.isEnabled());
                    edited.setSignature(codeValidationService.cleanSignature(codeTaskDTOFromUI.getSignature()));
                    edited.setTechnology(codeTaskDTOFromUI.getTechnology());
                    edited.getCorrectCodes().clear();
                    for (CorrectCode correctCodeToDelete: correctCodeRepository.findAllByCodeTask(edited)) {
                        correctCodeRepository.delete(correctCodeToDelete);
                    }
                    for (CorrectCode correctCodeToUpdate:codeTaskDTOFromUI.getCorrectCodes()) {
                        correctCodeToUpdate.setCodeTask(edited);
                        correctCodeRepository.save(correctCodeToUpdate);
                    }
                    codeTaskRepository.save(edited);
                } else {
                    throw new Exception("Signature validation not passed");
                }
            } else {
                throw new Exception("Task not found");
            }
        } catch (Exception e) {
            log.warn("Code task could not be updated. Stack trace: " + Arrays.toString(e.getStackTrace()));
            throw new Exception(e.getMessage());
        }
    }

    public void disableTask(Long id) throws Exception {
        try {
            Optional<CodeTask> optionalCodeTask = codeTaskRepository.findById(id);
            if (optionalCodeTask.isPresent()) {
                CodeTask codeTask = optionalCodeTask.get();
                if (codeTask.isEnabled()) {
                    codeTask.setEnabled(false);
                } else {
                    codeTask.setEnabled(true);
                }
                codeTaskRepository.save(codeTask);
            } else {
                throw new Exception("Task not found");
            }
        } catch (Exception e) {
            log.warn("Something went wrong while updating the code task, Stack trace: " + e.getStackTrace());
            throw new Exception("Code task's activity could not be updated");
        }
    }
}
