package com.internship.adminpanel.service;

import com.internship.adminpanel.model.AnswersOption;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.Task;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.answer_option.AnswerOptionDTO;
import com.internship.adminpanel.model.dto.task.TaskEditDTO;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final StreamRepository streamRepository;
    private final AnswersOptionRepository answersOptionRepository;

    public List<TaskListDTO> getAll() {
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
    public void addTask(TaskInsertDTO taskDto) {
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

    @Transactional
    public void editTask(TaskEditDTO taskEditDTO) {
        //Get Task from DB to edit
        Task taskToEdit = taskRepository.getOne(taskEditDTO.getId());

        //set new Task properties to be modified
        taskToEdit.setTitle(taskEditDTO.getTitle());
        taskToEdit.setDescription(taskEditDTO.getDescription());
        taskToEdit.setTaskType(taskEditDTO.getTaskType());
        taskToEdit.setComplexity(taskEditDTO.getComplexity());
        taskToEdit.setEnabled(taskEditDTO.isEnabled());

        //process & convert all StreamDTO in Stream
        //by StreamDto.getId() get the collection of Stream entities from DB
        //and assign them to the Task beign updated
        List<Stream> listNewStreams = new ArrayList<>();
        for (StreamDTO streamDto : taskEditDTO.getStreams()) {
            Stream streamDB = streamRepository.findById(streamDto.getId()).get();
            listNewStreams.add(streamDB);
        }
        taskToEdit.setStreams(listNewStreams);

        //-----------------------------
        //PROCESS ANSWER OPTIONS
        //-----------------------------

        //process & convert all AnswerOptionDTO in AnswerOption
        //(part I)      - Get AnswerOptionDTO without ID and create new AnswerOption entity for the current task ID
        //              - Add new AnswerOption entites to the listAnswerOptions
        //(part II)     - Get already existing AnswerOption by its ID which match to the ID arrived from view
        //              - Modify it
        //              - Get modified existing AnswerOption and add to the listAnswerOptions
        // (part III)   - Delete previously existed, but now removed by user AnswerOptions
        // (part IV)    - Add this new List of AnswerOptions to the task being modified

        List<AnswersOption> listNewAnswerOptions = new ArrayList<>();

        //(part I)
        listNewAnswerOptions.addAll(createAndReturnNewAnswerOptions(taskEditDTO,taskToEdit));

        //(part II)
        listNewAnswerOptions.addAll(modifyAndReturnExistingAnswerOptions(taskEditDTO));

        //(part III)
        deleteRemovedAnswerOptions(taskToEdit, listNewAnswerOptions);

        //(part IV)
        taskToEdit.setAnswersOptions(listNewAnswerOptions);
        taskRepository.save(taskToEdit);
    }

    public TaskEditDTO findById(Long id) {
        Optional<Task> taskDB = taskRepository.findById(id);
        if (taskDB.isPresent())
            return new TaskEditDTO(taskDB.get());
        else return new TaskEditDTO();
    }

    //region PRIVATE METHODS TO PROCESS EDIT TASK

    //methods to perform Part I of editTask public method
    private List<AnswersOption> createAndReturnNewAnswerOptions(TaskEditDTO taskEditDTO, Task taskToEdit){
        List<AnswersOption> listNewAnswerOptions = new ArrayList<>();
        List<AnswerOptionDTO> listAOwithNoId = taskEditDTO.getAnswers()
                .stream()
                .filter(ao -> ao.getId() == null)
                .collect(Collectors.toList());
        for (AnswerOptionDTO aoDTO : listAOwithNoId) {
            AnswersOption newAO = new AnswersOption(aoDTO);
            newAO.setTask(taskToEdit);
            AnswersOption newAOReturnedFromDB = answersOptionRepository.saveAndFlush(newAO);
            listNewAnswerOptions.add(newAOReturnedFromDB);
        }
        return listNewAnswerOptions;
    }

    //methods to perform Part II of editTask public method
    private List<AnswersOption> modifyAndReturnExistingAnswerOptions(TaskEditDTO taskEditDTO){
        List<AnswersOption> listNewAnswerOptions = new ArrayList<>();
        List<AnswerOptionDTO> listAOwithIDfromUI = taskEditDTO.getAnswers()
                .stream()
                .filter(ao -> ao.getId() != null)
                .collect(Collectors.toList());
        for (AnswerOptionDTO dto : listAOwithIDfromUI){
            AnswersOption answerToUpdate = answersOptionRepository.getOne(dto.getId());
            answerToUpdate.setAnswerOptionValue(dto.getAnswer());
            answerToUpdate.setCorrect(dto.isCorrect());
            AnswersOption updatedAOfromDB = answersOptionRepository.saveAndFlush(answerToUpdate);
            listNewAnswerOptions.add(updatedAOfromDB);
        }
        return listNewAnswerOptions;
    }

    //methods to perform Part III of editTask public method
    private void deleteRemovedAnswerOptions(Task taskToEdit, List<AnswersOption> listNewAnswerOptions){
        List<AnswersOption> listAOwithIDfromDB = taskToEdit.getAnswersOptions();
        //if this ID is not in new list -> remove from answer_option_table
        for (AnswersOption aoDB : listAOwithIDfromDB) {
            if (!listNewAnswerOptions.contains(aoDB)){
                answersOptionRepository.delete(aoDB);
            }
        }
    }
    //endregion

}
