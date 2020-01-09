package com.internship.adminpanel.service;

import com.internship.adminpanel.model.AnswersOption;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.Task;
import com.internship.adminpanel.model.dto.answer_option.AnswerOptionDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.task.TaskDisableDTO;
import com.internship.adminpanel.model.dto.task.TaskEditDTO;
import com.internship.adminpanel.model.dto.task.TaskInsertDTO;
import com.internship.adminpanel.model.dto.task.TaskListDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TypeEnum;
import com.internship.adminpanel.repository.AnswersOptionRepository;
import com.internship.adminpanel.repository.StreamRepository;
import com.internship.adminpanel.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceMockTest {

    @Mock
    TaskRepository taskRepositoryMock;

    @Mock
    StreamRepository streamRepositoryMock;

    @Mock
    AnswersOptionRepository answersOptionRepositoryMock;

    @InjectMocks
    TaskService taskServiceMock;

    @Test
    public void shouldGetAddTask() {

        //expected TaskListDTO objects
        List<TaskListDTO> expectedListDTO = new ArrayList<>();
        TaskListDTO taskOneDTO = new TaskListDTO( new Task(1L, "First Task", "Description One", TypeEnum.SINGLE_CHOICE, ComplexityEnum.EASY, true));
        TaskListDTO taskTwoDTO = new TaskListDTO(new Task(2L, "Second Task", "Description Two", TypeEnum.MULTI_CHOICE, ComplexityEnum.MEDIUM, false));
        TaskListDTO taskThreeDTO = new TaskListDTO(new Task(3L, "Third Task", "Description Three", TypeEnum.CUSTOM_QUESTION, ComplexityEnum.HARD, true));
        expectedListDTO.add(taskOneDTO);
        expectedListDTO.add(taskTwoDTO);
        expectedListDTO.add(taskThreeDTO);

        //DB Task records modulation
        List<Task> expectedListOfTaskFromDB = new ArrayList<>();
        Task taskOne = new Task(1L, "First Task", "Description One", TypeEnum.SINGLE_CHOICE, ComplexityEnum.EASY, true);
        Task taskTwo = new Task(2L, "Second Task", "Description Two", TypeEnum.MULTI_CHOICE, ComplexityEnum.MEDIUM, false);
        Task taskThree = new Task(3L, "Third Task", "Description Three", TypeEnum.CUSTOM_QUESTION, ComplexityEnum.HARD, true);
        expectedListOfTaskFromDB.add(taskOne);
        expectedListOfTaskFromDB.add(taskTwo);
        expectedListOfTaskFromDB.add(taskThree);

        //attached List of Task to the findAll method
        when(taskRepositoryMock.findAll()).thenReturn(expectedListOfTaskFromDB);

        //trigger TaskService method getAll()
        List<TaskListDTO> returnedListByService = taskServiceMock.getAll();

        //checks
        verify(taskRepositoryMock).findAll();
        assertThat(returnedListByService).isEqualTo(expectedListDTO);
    }

    @Test
    public void shouldAddTask(){
        //Created Discipline entity
        Discipline discipline = buildDisciplineEntity(1L, "Development");

        //Create Stream entities and List
        List<Stream> streams = new ArrayList<>();
        Stream stream = buildStreamEntity(1L, "JAVA", discipline);
        Stream stream2 = buildStreamEntity(2L, "DEVOPS", discipline);
        streams.add(stream);
        streams.add(stream2);

        //List of Stream IDs
        List<Long> streamIDs = new ArrayList<>();
        streamIDs.add(stream.getId());
        streamIDs.add(stream2.getId());

        when(streamRepositoryMock.findById(stream.getId())).thenReturn(Optional.of(stream));
        when(streamRepositoryMock.findById(stream2.getId())).thenReturn(Optional.of(stream2));

        //Create Task entity
        Task taskToBeSaved = buildTaskEntity(
                1L,
                "New Task Title",
                "New Task Description",
                TypeEnum.MULTI_CHOICE,
                ComplexityEnum.MEDIUM,
                streams,
                true);

        //Flushed task
        Task taskFlushed = taskToBeSaved;

        //Create List of AnswerOptionDTO and List of AnswerOption
        List<AnswerOptionDTO> answerOptionsDTO = new ArrayList<>();
        AnswerOptionDTO answerDTO_one = buildAODTO(1L, "Correct Answer", true);
        AnswerOptionDTO answerDTO_two = buildAODTO(2L, "Wrong Answer", false);
        answerOptionsDTO.add(answerDTO_one);
        answerOptionsDTO.add(answerDTO_two);
        List<AnswersOption> answerOptionsEntities = new ArrayList<>();
        for (AnswerOptionDTO dto :  answerOptionsDTO) {
            AnswersOption ao = convertAODTOintoAOEntity(dto, taskFlushed);
            answerOptionsEntities.add(ao);
        }

        when(answersOptionRepositoryMock.save(answerOptionsEntities.get(0))).thenReturn(answerOptionsEntities.get(0));
        when(answersOptionRepositoryMock.save(answerOptionsEntities.get(1))).thenReturn(answerOptionsEntities.get(1));
        when(taskRepositoryMock.saveAndFlush(any(Task.class))).thenReturn(taskFlushed);


        //Create TaskInsertDTO
        TaskInsertDTO taskInsertDTO = new TaskInsertDTO();
        taskInsertDTO.setTitle(taskToBeSaved.getTitle());
        taskInsertDTO.setDescription(taskToBeSaved.getDescription());
        taskInsertDTO.setTaskType(taskToBeSaved.getTaskType());
        taskInsertDTO.setComplexity(taskToBeSaved.getComplexity());
        taskInsertDTO.setEnabled(taskToBeSaved.isEnabled());
        taskInsertDTO.setAnswers(answerOptionsDTO);
        taskInsertDTO.setStreams(streamIDs);

        //trigger service
        taskServiceMock.addTask(taskInsertDTO);

        //check results
        verify(taskRepositoryMock).saveAndFlush(any(Task.class));
        verify(answersOptionRepositoryMock).save(answerOptionsEntities.get(0));
        verify(answersOptionRepositoryMock).save(answerOptionsEntities.get(1));
        verify(streamRepositoryMock).findById(1L);
        verify(streamRepositoryMock).findById(2L);
    }

    @Test
    public void shouldEditTask(){
        //Created Discipline entity
        Discipline discipline = buildDisciplineEntity(1L, "Development");

        //Create Stream entities and List
        List<Stream> streams = new ArrayList<>();
        Stream stream = buildStreamEntity(1L, "JAVA", discipline);
        Stream stream2 = buildStreamEntity(2L, "DEVOPS", discipline);
        stream.setSkill(new ArrayList<>());
        stream2.setSkill(new ArrayList<>());
        stream.setTestStructures(new ArrayList<>());
        stream2.setTestStructures(new ArrayList<>());
        streams.add(stream);
        streams.add(stream2);

        //Get Task from DB to edit
        Task taskToEdit = buildTaskEntity(
                1L,
                "New Task Title",
                "New Task Description",
                TypeEnum.MULTI_CHOICE,
                ComplexityEnum.MEDIUM,
                streams,
                true);

        //Create List of AnswerOptionDTO and List of AnswerOption
        List<AnswerOptionDTO> answerOptionsDTO = new ArrayList<>();
        AnswerOptionDTO answerDTO_one = buildAODTO(1L, "Correct Answer", true);
        AnswerOptionDTO answerDTO_two = buildAODTO(2L, "Wrong Answer", false);
        answerOptionsDTO.add(answerDTO_one);
        answerOptionsDTO.add(answerDTO_two);
        List<AnswersOption> answerOptionsEntities = new ArrayList<>();
        for (AnswerOptionDTO dto :  answerOptionsDTO) {
            AnswersOption ao = convertAODTOintoAOEntity(dto, taskToEdit);
            answerOptionsEntities.add(ao);
        }

        taskToEdit.setAnswersOptions(answerOptionsEntities);

        TaskEditDTO editDTO = new TaskEditDTO(taskToEdit);

        List<Stream> listNewStreams = new ArrayList<>();
        for (StreamDTO streamDto: editDTO.getStreams()) {
            Stream streamEntity = new Stream();
            streamEntity.setId(streamDto.getId());
            streamEntity.setName(streamDto.getName());
            listNewStreams.add(streamEntity);
        }
        taskToEdit.setStreams(listNewStreams);

        List<AnswersOption> listNewAnswerOptions = new ArrayList<>();

        when(taskRepositoryMock.getOne(taskToEdit.getId())).thenReturn(taskToEdit);
        when(streamRepositoryMock.findById(1L)).thenReturn(Optional.of(stream));
        when(streamRepositoryMock.findById(2L)).thenReturn(Optional.of(stream2));

        //Modify Task
        taskToEdit.setTitle("Modified Title");
        taskToEdit.setDescription("Modified Description");
        taskToEdit.setTaskType(TypeEnum.CUSTOM_QUESTION);
        taskToEdit.setComplexity(ComplexityEnum.HARD);
        taskToEdit.setEnabled(false);

        //trigger service
        taskServiceMock.editTask(editDTO);
        verify(taskRepositoryMock).getOne(taskToEdit.getId());
        verify(streamRepositoryMock).findById(1L);
        verify(streamRepositoryMock).findById(2L);
    }

    @Test
    public void shouldFindById(){
        Task taskDB = buildTaskEntity(
                3L,
                "Found by IDTitle",
                "Found by ID Description",
                TypeEnum.CUSTOM_QUESTION,
                ComplexityEnum.EASY,
                new ArrayList<>(),
                true);
        taskDB.setAnswersOptions(new ArrayList<>());

//        TaskEditDTO editDto = new TaskEditDTO();
        TaskEditDTO editDtoConverted = new TaskEditDTO(taskDB);

        when(taskRepositoryMock.findById(taskDB.getId())).thenReturn(Optional.of(taskDB));
        TaskEditDTO found = taskServiceMock.findById(taskDB.getId());
        verify(taskRepositoryMock).findById(taskDB.getId());
        assertThat(editDtoConverted).isEqualTo(editDtoConverted);
    }

    @Test
    public void shouldDisableTask(){
        Task taskDB = buildTaskEntity(
                4L,
                "To be disabled",
                "To be disabled",
                TypeEnum.CUSTOM_QUESTION,
                ComplexityEnum.EASY,
                new ArrayList<>(),
                true);
        taskDB.setAnswersOptions(new ArrayList<>());

        TaskDisableDTO disabledTask = new TaskDisableDTO();
        disabledTask.setId(taskDB.getId());
        disabledTask.setEnabled(false);

        taskDB.setEnabled(disabledTask.isEnabled());

        when(taskRepositoryMock.getOne(taskDB.getId())).thenReturn(taskDB);
        Task returned = taskRepositoryMock.getOne(taskDB.getId());
        assertThat(returned).isEqualTo(taskDB);

        taskServiceMock.disableTask(disabledTask);
    }

    //--------------------------------------------------------------------------------------------
    //--- HELPERS ---
    //--------------------------------------------------------------------------------------------

    //region ENTITY-BUILDERS
    private Discipline buildDisciplineEntity(Long id, String name){
        Discipline entity = new Discipline();
        entity.setId(id);
        entity.setName(name);
        return entity;
    }

    private Stream buildStreamEntity(Long id, String name, Discipline discipline){
        Stream stream = new Stream();
        stream.setId(id);
        stream.setName(name);
        stream.setDiscipline(discipline);
        return stream;
    }

    private Task buildTaskEntity(Long id, String title, String desc, TypeEnum type, ComplexityEnum comp, List<Stream> streams, boolean activate){
        Task entity = new Task();
        entity.setId(id);
        entity.setTitle(title);
        entity.setDescription(desc);
        entity.setTaskType(type);
        entity.setComplexity(comp);
        entity.setStreams(streams);
        entity.setEnabled(activate);
        return entity;
    }
    //endregion

    //region DTO-BUILDERS
    private AnswerOptionDTO buildAODTO(Long id, String answer, boolean correct){
        AnswerOptionDTO dto = new AnswerOptionDTO();
        dto.setId(id);
        dto.setAnswer(answer);
        dto.setCorrect(correct);
        return dto;
    }
    //endregion

    //region CONVERTERS FROM DTO into ENTITY
    private AnswersOption convertAODTOintoAOEntity(AnswerOptionDTO dto, Task task){
        AnswersOption entity = new AnswersOption();
        entity.setAnswerOptionValue(dto.getAnswer());
        entity.setCorrect(dto.isCorrect());
        entity.setTask(task);
        return entity;
    }
    //endregion
}
