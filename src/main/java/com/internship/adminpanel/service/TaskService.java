package com.internship.adminpanel.service;

import com.internship.adminpanel.model.AnswersOption;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.Task;
import com.internship.adminpanel.model.dto.answer_option.AnswerOptionDTO;
import com.internship.adminpanel.model.dto.task.TaskInsertDTO;
import com.internship.adminpanel.model.dto.task.TaskListDTO;
import com.internship.adminpanel.repository.AnswersOptionRepository;
import com.internship.adminpanel.repository.StreamRepository;
import com.internship.adminpanel.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final StreamRepository streamRepository;
    private final AnswersOptionRepository answersOptionRepository;

    public List<TaskListDTO> getAll(){
        ArrayList<TaskListDTO> tasksDto = new ArrayList<TaskListDTO>();
        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks = (ArrayList<Task>) taskRepository.findAll();
        for (Task task : tasks) {
            TaskListDTO taskDTO = new TaskListDTO(task);
            tasksDto.add(taskDTO);
        }
        return tasksDto;
    }

    @Transactional
    public void addTask(TaskInsertDTO taskDto){
        //Create Task entity and save it
        Task task = new Task(taskDto);

        List<Stream> listStreamsToPersist = new ArrayList<>();
        //create a collation of Streams to persist
        for (Long streamID : taskDto.getStreams()) {
            //get Stream object
            Stream stream = new Stream();
            if (streamRepository.findById(streamID).isPresent()) {
                stream = streamRepository.findById(streamID).get();
                listStreamsToPersist.add(stream);
            }
            task.setStreams(listStreamsToPersist);
        }
        Task newlyCreatedTask = taskRepository.saveAndFlush(task);
        Long newlyCreatedTaskId = newlyCreatedTask.getId();

        //populate answer_option_table
        for (AnswerOptionDTO answerDto : taskDto.getAnswers()) {
            AnswersOption answersOption = new AnswersOption();
            answersOption.setTask(newlyCreatedTask);
            answersOption.setAnswerOptionValue(answerDto.getAnswer());
            answersOption.setCorrect(answerDto.isCorrect());
            answersOptionRepository.save(answersOption);
        }
    }
}
