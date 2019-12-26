package com.internship.adminpanel.service;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.CorrectCode;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.code_task.AnswersSubmitDTO;
import com.internship.adminpanel.model.dto.code_task.CodeTaskSubmitDTO;
import com.internship.adminpanel.model.dto.task.CodeTaskDTOFromUI;
import com.internship.adminpanel.repository.CodeTaskRepository;
import com.internship.adminpanel.repository.CorrectCodeRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class CodeTaskService {
    private final CodeTaskRepository codeTaskRepository;
    private final StreamRepository streamRepository;
    private final CodeValidationService codeValidationService;
    private final CorrectCodeRepository correctCodeRepository;

    public void saveTask(CodeTaskSubmitDTO codeTaskSubmitDTO) throws Exception {
        try {
            if (codeValidationService.validateSignature(codeTaskSubmitDTO.getSignature())) {
                for (AnswersSubmitDTO answer:codeTaskSubmitDTO.getAnswersSubmit()) {
                    if (!codeValidationService.validateAnswers(answer)) {
                        throw new Exception("Answer tests validation not passed");
                    }
                }
                CodeTaskDTOFromUI codeTaskDTOFromUI = new CodeTaskDTOFromUI();
                codeTaskDTOFromUI.setTitle(codeTaskSubmitDTO.getTitle());
                codeTaskDTOFromUI.setComplexity(codeTaskSubmitDTO.getComplexity());
                codeTaskDTOFromUI.setTechnology(codeTaskSubmitDTO.getTechnology());
                List<CorrectCode> currentCodes = new ArrayList<>();
                for (AnswersSubmitDTO answer:codeTaskSubmitDTO.getAnswersSubmit()) {
                    CorrectCode inLoopCorrectCode = new CorrectCode(answer);
                    currentCodes.add(inLoopCorrectCode);
                }
                codeTaskDTOFromUI.setCorrectCodes(currentCodes);
                codeTaskDTOFromUI.setDescription(codeTaskSubmitDTO.getDescription());
                codeTaskDTOFromUI.setSignature(codeValidationService.cleanSignature(codeTaskSubmitDTO.getSignature()));
                codeTaskDTOFromUI.setEnabled(codeTaskSubmitDTO.isEnabled());
                CodeTask tobeSaved = new CodeTask(codeTaskDTOFromUI);
                for (CorrectCode code: currentCodes) {
                    code.setCodeTask(tobeSaved);
                }
                List<Stream> codeTaskStreams = new ArrayList<>();
                for (Long s: codeTaskSubmitDTO.getStreams()) {
                    codeTaskStreams.add(streamRepository.findById(s).get());
                }
                tobeSaved.setStreams(codeTaskStreams);
                codeTaskRepository.save(tobeSaved);
                for (CorrectCode code: currentCodes) {
                    correctCodeRepository.save(code);
                }
            } else throw new Exception("Signature validation not passed");
        } catch (Exception e) {
            throw new Exception("Task could not be saved, check all the data again. \nReason: " + e.getMessage());
        }
    }
}
