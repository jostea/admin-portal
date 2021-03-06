package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.*;
import com.internship.adminpanel.model.*;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTOFromUI;
import com.internship.adminpanel.repository.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Data
@RunWith(MockitoJUnitRunner.Silent.class)
public class StreamServiceTest {

    public StreamServiceTest() {
    }

    @Mock
    private StreamRepository streamRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InternshipRepository internshipRepository;

    @Mock
    private TestStructureRepository testStructureRepository;

    @Mock
    private StreamTimeRepository streamTimeRepository;

    @InjectMocks
    private StreamService streamService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldAddStream() throws Exception {
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        Discipline discipline = createDiscipline("awesomeDiscipline");
        Stream stream = createStreamFromDTOAndDiscipline(streamDTOFromUI, discipline);
        //In when use mocks, behavior of mocked methods in service
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.of(discipline));
        //trigger service
        streamService.addStream(streamDTOFromUI);
        //check result
        verify(disciplineRepository).findById(streamDTOFromUI.getDisciplineId());
        verify(streamRepository).save(stream);
    }

    @Test
    public void shouldUpdateStream() throws Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        Discipline discipline = createDiscipline("TestDiscipline");
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.of(discipline));
        Stream stream = createStreamByNameAndDiscipline("TestStream", discipline);
        when(streamRepository.findById(id)).thenReturn(Optional.of(stream));
        streamService.edit(id, streamDTOFromUI, "TestUser");
        verify(disciplineRepository).findById(streamDTOFromUI.getDisciplineId());
        verify(streamRepository).findById(id);
        verify(streamRepository).save(stream);
    }

    @Test
    public void shouldFindAllStreams() throws Exception {
        List<StreamDTO> expectedStreamList = new ArrayList<>();
        expectedStreamList.add(
                createStreamDTOWithSpecificStreamAndDisciplineNames("TestStream1", "DisciplineTest1"));
        expectedStreamList.add(
                createStreamDTOWithSpecificStreamAndDisciplineNames("TestStream2", "DisciplineTest2"));
        //DB
        List<Stream> mockedDbStreams = new ArrayList<>();
        mockedDbStreams.add(createStreamByNameAndDiscipline("TestStream1", createDiscipline("DisciplineTest1")));
        mockedDbStreams.add(createStreamByNameAndDiscipline("TestStream2", createDiscipline("DisciplineTest2")));

        when(streamRepository.findAll()).thenReturn(mockedDbStreams);
        //triggered service
        List<StreamDTO> returnedStreamList = streamService.findAll();
        //checking that: findAll called on streamRepository and result is equal to expected one
        verify(streamRepository).findAll();
        assertThat(returnedStreamList).isEqualTo(expectedStreamList);
    }

    @Test
    public void shouldReturnListOfStreamSpecifiedByName() throws Exception {
        List<StreamDTO> expectedStreamList = new ArrayList<>();
        expectedStreamList.add(
                createStreamDTOWithSpecificStreamAndDisciplineNames("TestStream2", "DisciplineTest2"));
        List<Stream> mockedDbStreams = new ArrayList<>();
        mockedDbStreams.add(createStreamByNameAndDiscipline("TestStream2", createDiscipline("DisciplineTest2")));
        String name = "2";
        when(streamRepository.findStreamByNameContainingIgnoreCase(name)).thenReturn(mockedDbStreams);
        List<StreamDTO> returnedStreamDTOList = streamService.filterByName(name);
        verify(streamRepository).findStreamByNameContainingIgnoreCase(name);
        assertThat(returnedStreamDTOList).isEqualTo(expectedStreamList);
    }

    @Test
    public void shouldFindStreamById() throws StreamNotFound {
        Long id = 1L;
        Stream expectedStream = createStreamByNameAndDiscipline("streamName", createDiscipline("discipline1"));
        StreamDTO expectedStreamDTO = new StreamDTO(expectedStream);
        when(streamRepository.findById(id)).thenReturn(Optional.of(expectedStream));
        StreamDTO foundStream = streamService.findById(id);
        verify(streamRepository).findById(id);
        assertThat(expectedStreamDTO).isEqualTo(foundStream);
    }

    @Test
    public void shouldDeleteStream() throws StreamHasCandidate, StreamNotFound {
        Long id = 1L;
        List<TestStructure> ts = new ArrayList<>();
        ts.add(TestStructure.builder().id(1L).build());

        List<Internship> internships = new ArrayList<>();
        List<Stream> streams = new ArrayList<>();

        List<Task> tasks = new ArrayList<>();
        tasks.add(Task.builder().streams(streams).build());

        List<CodeTask> codeTasks = new ArrayList<>();
        codeTasks.add(CodeTask.builder().streams(streams).build());

        List<SqlTask> sqlTask = new ArrayList<>();
        sqlTask.add(SqlTask.builder().streams(streams).build());

        List<Skill> skills = new ArrayList<>();
        skills.add(Skill.builder().streams(streams).build());

        Stream stream = Stream.builder()
                .id(1L)
                .name("TestStream")
                .discipline(createDiscipline("TestDiscipline"))
                .tasks(tasks)
                .testStructures(ts)
                .skill(skills)
                .streamTime(StreamTime.builder().id(1L).build())
                .codeTasks(codeTasks)
                .sqlTasks(sqlTask)
                .build();
        streams.add(stream);
        internships.add(Internship.builder().streams(streams).build());
        when(candidateRepository.findCandidateByStream(stream)).thenReturn(new ArrayList<>());
        when(streamRepository.findById(id)).thenReturn(Optional.of(stream));
        when(internshipRepository.findAll()).thenReturn(internships);
        when(testStructureRepository.findAll()).thenReturn(new ArrayList<>());
        //Trigger delete method
        streamService.deleteById(id);

        verify(candidateRepository).findCandidateByStream(stream);
        verify(streamRepository).findById(id);
        verify(internshipRepository).findAll();
        verify(testStructureRepository).deleteById(1L);
        verify(streamTimeRepository).deleteById(1L);
        verify(streamRepository).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionCandidateExistWhileDeleteStream() throws StreamHasCandidate, StreamNotFound {
        Long id = 1L;
        exception.expect(StreamHasCandidate.class);
        exception.expectMessage(id + "");
        Stream stream = new Stream();
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(Candidate.builder().stream(stream).build());
        when(streamRepository.findById(id)).thenReturn(Optional.of(stream));
        when(candidateRepository.findCandidateByStream(stream)).thenReturn(candidates);

        streamService.deleteById(id);

        verify(streamRepository).findById(id);
        verify(candidateRepository).findCandidateByStream(stream);
    }

    @Test
    public void shouldThrowExceptionEmptyNameWhileEditStream() throws EmptyName, SQLException, DisciplineNotFound, StreamNotFound {
        exception.expect(EmptyName.class);
        exception.expectMessage("");
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .disciplineId(1L).name("")
                .build();
        Discipline discipline = createDiscipline("TestDiscipline");
        Stream stream = createStreamByNameAndDiscipline("TestStream", discipline);

        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.of(discipline));
        when(streamRepository.findById(id)).thenReturn(Optional.of(stream));

        streamService.edit(id, streamDTOFromUI, "TestUser");
        verify(disciplineRepository).findById(streamDTOFromUI.getDisciplineId());
        verify(streamRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionEmptyNameWhileAddStream() throws DisciplineNotFound, EmptyName, Exception {
        exception.expect(EmptyName.class);
        exception.expectMessage("");
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("")
                .disciplineId(1L)
                .build();
        Discipline discipline = createDiscipline("Labood");
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.of(discipline));
        streamService.addStream(streamDTOFromUI);
        verify(disciplineRepository).findById(streamDTOFromUI.getDisciplineId());
    }

    @Test
    public void shouldThrowExceptionWhileSearchingStreamById() throws StreamNotFound {
        Long id = 100L;
        exception.expect(StreamNotFound.class);
        exception.expectMessage(id + "");
        when(streamRepository.findById(id)).thenReturn(Optional.empty());
        streamService.findById(id);
    }

    @Test
    public void shouldThrowExceptionWhileSearchingStreamByName() throws Exception {
        List<Stream> streamList = new ArrayList<>();
        String name = "TestName";
        exception.expect(StreamNotFound.class);
        exception.expectMessage(name);
        when(streamRepository.findStreamByNameContainingIgnoreCase(name)).thenReturn(streamList);
        streamService.filterByName(name);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhileEditStream() throws Exception {
        Long id = 1L;
        exception.expect(StreamNotFound.class);
        exception.expectMessage(id + "");
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        Discipline discipline = createDiscipline("TestDiscipline");
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.of(discipline));
        when(streamRepository.findById(id)).thenReturn(Optional.empty());
        streamService.edit(id, streamDTOFromUI, "TestUser");
    }

    @Test
    public void shouldThrowExceptionDisciplineNotFoundWhileEditStream() throws DisciplineNotFound, SQLException, StreamNotFound, EmptyName {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        String username = "TestUsername";
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id + "");
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.empty());
        streamService.edit(id, streamDTOFromUI, username);
    }

    @Test
    public void shouldThrowExceptionWhileEditStream() throws DisciplineNotFound, SQLException, StreamNotFound, EmptyName {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        String username = "TestUsername";
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id + "");
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.empty());
        streamService.edit(id, streamDTOFromUI, username);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhileDeleteStream() throws StreamNotFound, StreamHasTasks, StreamHasSkill, StreamHasCandidate {
        Long id = 1L;
        exception.expect(StreamNotFound.class);
        exception.expectMessage(id + "");
        when(streamRepository.findById(id)).thenReturn(Optional.empty());
        streamService.deleteById(id);
        verify(streamRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionWhileDeleteByIdAndOptionalIsEmpty() throws Exception {
        exception.expect(Exception.class);
        Long id = 1L;
        when(streamRepository.findById(id)).thenReturn(Optional.empty());
        streamService.deleteById(id);
        verify(streamRepository).findById(id);
    }

    @Test(expected = Exception.class)
    public void shouldReturnedExceptionWhileFindById() throws Exception {
        String name = "Cat";
        doThrow(new Exception()).when(streamRepository).findStreamByNameContainingIgnoreCase(name);
        streamService.filterByName(name);
        verify(streamRepository).findStreamByNameContainingIgnoreCase(name);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhileGetAllStreams() throws Exception {
        doThrow(new Exception()).when(streamRepository).findAll();
        streamService.findAll();
        verify(streamRepository).findAll();
    }

    @Test
    public void shouldThrowExceptionDisciplineNotFoundWhileAddNewStream() throws Exception {
        Long id=1L;
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id+"");
        StreamDTOFromUI streamDTOFromUI=createStreamDTOFromUI();
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.empty());
        streamService.addStream(streamDTOFromUI);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhileAddNewStream() throws Exception {
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        Stream stream = createStreamByNameAndDiscipline("Chiort", createDiscipline("Lysyi"));
        doThrow(new Exception()).when(streamRepository).save(stream);
        streamService.addStream(streamDTOFromUI);
        verify(streamRepository).save(stream);
    }


    private StreamDTO createStreamDTOWithSpecificStreamAndDisciplineNames(String streamName, String disciplineName) {
        return StreamDTO.builder()
                .id(1L)
                .name(streamName)
                .disciplineName(disciplineName)
                .skills(new ArrayList<>())
                .testStructures(new ArrayList<>())
                .build();
    }

    private Stream createStreamByNameAndDiscipline(String name, Discipline discipline) {
        return Stream.builder()
                .id(1L)
                .name(name)
                .discipline(discipline)
                .skill(new ArrayList<>())
                .testStructures(new ArrayList<>())
                .build();
    }


    private StreamDTOFromUI createStreamDTOFromUI() {
        return StreamDTOFromUI.builder()
                .name("TestStreamFromUI")
                .disciplineId(1L)
                .build();
    }

    private Discipline createDiscipline(String name) {
        return Discipline.builder()
                .name(name)
                .build();
    }

    private Stream createStreamFromDTOAndDiscipline(StreamDTOFromUI streamDTOFromUI, Discipline discipline) {
        return Stream.builder()
                .name(streamDTOFromUI.getName())
                .discipline(discipline)
                .build();
    }
}
