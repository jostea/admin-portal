package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTOFromUI;
import com.internship.adminpanel.repository.DisciplineRepository;
import com.internship.adminpanel.repository.StreamRepository;
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

@RunWith(MockitoJUnitRunner.class)
public class StreamServiceTest {

    @Mock
    private StreamRepository streamRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private StreamService streamService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldAddStream() throws DisciplineNotFound, SQLException {
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
    public void shouldUpdateStream() throws StreamNotFound, DisciplineNotFound, SQLException {
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
    public void shouldFindAllStreams() {
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
    public void shouldReturnListOfStreamSpecifiedByName() throws StreamNotFound {
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
    public void shouldDeleteStream() {
        Long id = 1L;
        streamService.deleteById(id);
        verify(streamRepository).deleteById(id);
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
    public void shouldThrowExceptionWhileSearchingStreamByName() throws StreamNotFound {
        List<Stream> streamList = new ArrayList<>();
        String name = "TestName";
        exception.expect(StreamNotFound.class);
        exception.expectMessage(name);
        when(streamRepository.findStreamByNameContainingIgnoreCase(name)).thenReturn(streamList);
        streamService.filterByName(name);
    }

    @Test
    public void shouldThrowExceptionStreamNotFoundWhileEditStream() throws StreamNotFound, DisciplineNotFound, SQLException {
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
    public void shouldThrowExceptionDisciplineNotFoundWhileEditStream() throws DisciplineNotFound, SQLException, StreamNotFound {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = createStreamDTOFromUI();
        String username = "TestUsername";
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id + "");
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.empty());
        streamService.edit(id, streamDTOFromUI, username);
    }

    @Test
    public void shouldThrowExceptionDisciplineNotFoundWhileAddNewStream() throws DisciplineNotFound {
        Long id=1L;
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id+"");
        StreamDTOFromUI streamDTOFromUI=createStreamDTOFromUI();
        when(disciplineRepository.findById(streamDTOFromUI.getDisciplineId())).thenReturn(Optional.empty());
        streamService.addStream(streamDTOFromUI);
    }
    private StreamDTO createStreamDTOWithSpecificStreamAndDisciplineNames(String streamName, String disciplineName) {
        return StreamDTO.builder()
                .id(1L)
                .name(streamName)
                .disciplineName(disciplineName)
                .build();
    }

    private Stream createStreamByNameAndDiscipline(String name, Discipline discipline) {
        return Stream.builder()
                .id(1L)
                .name(name)
                .discipline(discipline)
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
