package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.*;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTOFromUI;
import com.internship.adminpanel.service.StreamService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StreamRestControllerTest {

    @Mock
    private StreamService streamService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private StreamRestController streamRestController;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldReturnedAllStreams() throws Exception {
        List<StreamDTO> expectedList = new ArrayList<>();
        expectedList.add(createStreamDTO("TestStream2", "TestDiscipline2"));
        expectedList.add(createStreamDTO("TestStream1", "TestDiscipline1"));
        List<StreamDTO> mockedListStream = new ArrayList<>();
        mockedListStream.add(createStreamDTO("TestStream2", "TestDiscipline2"));
        mockedListStream.add(createStreamDTO("TestStream1", "TestDiscipline1"));
        when(streamService.findAll()).thenReturn(mockedListStream);
        ResponseEntity<List<StreamDTO>> returnedResponse = streamRestController.streams(authentication);
        verify(streamService).findAll();
        assertThat(returnedResponse.getBody()).isEqualTo(expectedList);
        assertThat(returnedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnedStreamDTOSpecifiedById() throws StreamNotFound {
        Long id = 1L;
        StreamDTO expectedStreamDTO = createStreamDTO("TestStream1", "TestDiscipline1");
        StreamDTO mockedStream = createStreamDTO("TestStream1", "TestDiscipline1");
        when(streamService.findById(id)).thenReturn(mockedStream);
        ResponseEntity<StreamDTO> returnedResponse = streamRestController.getById(id, authentication);
        verify(streamService).findById(id);
        assertThat(returnedResponse.getBody()).isEqualTo(expectedStreamDTO);
        assertThat(returnedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteStreamSpecifiedById() throws Exception {
        Long id = 1L;
        ResponseEntity<String> returnedResult = streamRestController.deletedById(id, authentication);
        verify(streamService).deleteById(id);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnedStreamSpecifiedByName() throws Exception {
        List<StreamDTO> expectedList = new ArrayList<>();
        expectedList.add(createStreamDTO("TestStream2", "TestDiscipline2"));
        expectedList.add(createStreamDTO("TestStream1", "TestDiscipline1"));
        List<StreamDTO> mockedListStream = new ArrayList<>();
        mockedListStream.add(createStreamDTO("TestStream2", "TestDiscipline2"));
        mockedListStream.add(createStreamDTO("TestStream1", "TestDiscipline1"));
        String name = "Test";
        when(streamService.filterByName(name)).thenReturn(mockedListStream);
        ResponseEntity<List<StreamDTO>> returnedResult = streamRestController.findByName(name, authentication);
        verify(streamService).filterByName(name);
        assertThat(returnedResult.getBody()).isEqualTo(expectedList);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldUpdateStream() throws Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("TestStreamFromUi")
                .disciplineId(1L)
                .build();
        ResponseEntity<String> returnedResult = streamRestController.update(id, streamDTOFromUI, authentication);
        verify(streamService).edit(id, streamDTOFromUI, null);
        assertThat(returnedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldAddStream() throws Exception {
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("TestStream")
                .disciplineId(1L)
                .build();
        ResponseEntity<String> returnedResponse = streamRestController.addStream(streamDTOFromUI, authentication);
        verify(streamService).addStream(streamDTOFromUI);
        assertThat(returnedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCatchExceptionStreamNotFoundWhileFindById() throws StreamNotFound {
        Long id = 1L;
        when(streamService.findById(id)).thenThrow(new StreamNotFound(id + ""));
        ResponseEntity<StreamDTO> returnedResponse = streamRestController.getById(id, authentication);
        verify(streamService).findById(id);
        assertThat(returnedResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCatchExceptionStreamNotWhileFindByName() throws Exception {
        String name = "Doggy";
        when(streamService.filterByName(name)).thenThrow(new StreamNotFound(name));
        ResponseEntity<List<StreamDTO>> returnedResponse = streamRestController.findByName(name, authentication);
        verify(streamService).filterByName(name);
        assertThat(returnedResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCatchExceptionWhileFilterByName() throws Exception {
        String name = "Ashan";
        doThrow(new Exception()).when(streamService).filterByName(name);
        ResponseEntity<List<StreamDTO>> responseEntity = streamRestController.findByName(name, authentication);
        verify(streamService).filterByName(name);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionWhileGetAllStreams()throws Exception {
        when(streamService.findAll()).thenThrow(new Exception());
        ResponseEntity<List<StreamDTO>> responseEntity = streamRestController.streams(authentication);
        verify(streamService).findAll();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionStreamNotFoundWhileEditStream() throws Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("TestStreamFromUI")
                .disciplineId(1L)
                .build();
        doThrow(new StreamNotFound(id + "")).when(streamService).edit(id, streamDTOFromUI, null);
        ResponseEntity<String> responseEntity = streamRestController.update(id, streamDTOFromUI, authentication);
        verify(streamService).edit(id, streamDTOFromUI, null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionDisciplineNotFoundWhileEditStream() throws DisciplineNotFound, SQLException, StreamNotFound, Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("TestStreamFromUI")
                .disciplineId(1L)
                .build();
        doThrow(new DisciplineNotFound(id + "")).when(streamService).edit(id, streamDTOFromUI, null);
        ResponseEntity<String> responseEntity = streamRestController.update(id, streamDTOFromUI, authentication);
        verify(streamService).edit(id, streamDTOFromUI, null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchSQLExceptionWhileEditStream() throws DisciplineNotFound, SQLException, StreamNotFound, Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("TestStreamFromUI")
                .disciplineId(1L)
                .build();
        doThrow(new SQLException()).when(streamService).edit(id, streamDTOFromUI, null);
        ResponseEntity<String> responseEntity = streamRestController.update(id, streamDTOFromUI, authentication);
        verify(streamService).edit(id, streamDTOFromUI, null);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionDisciplineNotFoundWhileAddStream() throws Exception {
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("TestStreamFromUI")
                .disciplineId(1L)
                .build();
        doThrow(new DisciplineNotFound(streamDTOFromUI.getDisciplineId() + "")).when(streamService).addStream(streamDTOFromUI);
        ResponseEntity<String> responseEntity = streamRestController.addStream(streamDTOFromUI, authentication);
        verify(streamService).addStream(streamDTOFromUI);
        assertThat(responseEntity.getBody()).isEqualTo("Discipline didn't specified");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void shouldCatchExceptionWhileAddStream() throws Exception {
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("Berbek")
                .disciplineId(1L)
                .build();
        doThrow(new Exception()).when(streamService).addStream(streamDTOFromUI);
        ResponseEntity<String> responseEntity = streamRestController.addStream(streamDTOFromUI, authentication);
        verify(streamService).addStream(streamDTOFromUI);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionAlreadyExistWhileAddNewStream() throws DataIntegrityViolationException, Exception {
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("AbgarMangal")
                .disciplineId(1L)
                .build();
        doThrow(DataIntegrityViolationException.class).when(streamService).addStream(streamDTOFromUI);
        ResponseEntity<String> responseEntity = streamRestController.addStream(streamDTOFromUI, authentication);
        verify(streamService).addStream(streamDTOFromUI);
        assertThat(responseEntity.getBody()).isEqualTo("Stream '" + streamDTOFromUI.getName() + "' already exist");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionEmptyNameWhileAddStream() throws Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("")
                .disciplineId(id)
                .build();
        doThrow(EmptyName.class).when(streamService).addStream(streamDTOFromUI);
        ResponseEntity<String> responseEntity = streamRestController.addStream(streamDTOFromUI, authentication);
        verify(streamService).addStream(streamDTOFromUI);
        assertThat(responseEntity.getBody()).isEqualTo("Stream name is required");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionStreamNotFoundWhileDelete() throws StreamNotFound, StreamHasTasks, StreamHasSkill, StreamHasCandidate {
        Long id = 1L;
        doThrow(StreamNotFound.class).when(streamService).deleteById(id);
        ResponseEntity<String> responseEntity = streamRestController.deletedById(id, authentication);
        verify(streamService).deleteById(id);
        assertThat(responseEntity.getBody()).isEqualTo("This stream not found");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCatchExceptionStreamHasTasksWhileDelete() throws StreamNotFound, StreamHasTasks, StreamHasSkill, StreamHasCandidate {
        Long id = 1L;
        doThrow(StreamHasCandidate.class).when(streamService).deleteById(id);
        ResponseEntity<String> responseEntity = streamRestController.deletedById(id, authentication);
        verify(streamService).deleteById(id);
        assertThat(responseEntity.getBody()).isEqualTo("This stream has candidate");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCatchExceptionEmptyNameWhileUpdateStream() throws Exception {
        Long id = 1L;
        StreamDTOFromUI streamDTOFromUI = StreamDTOFromUI.builder()
                .name("")
                .disciplineId(id)
                .build();
        doThrow(EmptyName.class).when(streamService).edit(id, streamDTOFromUI, null);
        ResponseEntity<String> responseEntity = streamRestController.update(id, streamDTOFromUI, authentication);
        verify(streamService).edit(id, streamDTOFromUI, null);
        assertThat(responseEntity.getBody()).isEqualTo("Stream name is required");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public StreamDTO createStreamDTO(String name, String disciplineName) {
        return StreamDTO.builder()
                .id(1L)
                .name(name)
                .disciplineName(disciplineName)
                .disciplineId(1L)
                .build();
    }
}
