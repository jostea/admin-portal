package com.internship.adminpanel.service;

import com.internship.adminpanel.model.CodeTask;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.task.CodeTaskDTOFromUI;
import com.internship.adminpanel.repository.CodeTaskRepository;
import com.internship.adminpanel.repository.CorrectCodeRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CodeTaskService {
    private final CodeTaskRepository codeTaskRepository;
    private final StreamRepository streamRepository;
    private final CorrectCodeRepository correctCodeRepository;

    public void saveTask(CodeTaskDTOFromUI codeTaskDTOFromUI) throws Exception {
        try {
            CodeTask codeTask = new CodeTask(codeTaskDTOFromUI);
            List<Stream> codeTaskStreams = new ArrayList<>();
            for (Long s: codeTaskDTOFromUI.getStreams()) {
                codeTaskStreams.add(streamRepository.findById(s).get());
            }
            codeTask.setStreams(codeTaskStreams);
            codeTaskRepository.save(codeTask);
            for (int i = 0; i < codeTaskDTOFromUI.getCorrectCodes().size(); i++) {
                codeTaskDTOFromUI.getCorrectCodes().get(i).setCodeTask(codeTask);
                correctCodeRepository.save(codeTaskDTOFromUI.getCorrectCodes().get(i));
            }
        } catch (Exception e) {
            throw new Exception("Task could not be saved, check all the data again");
        }
    }
}
