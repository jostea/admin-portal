package com.internship.adminpanel.controller;

import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.repository.DisciplineRepository;
import com.internship.adminpanel.service.StreamService;
import com.internship.adminpanel.repository.StreamRepository;
import com.internship.adminpanel.restcontroller.StreamRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StreamRestControllerTest {

    @Mock
    private StreamService streamService;

    @InjectMocks
    private StreamRestController streamRestController;
    //TODO test for get all streams in rest controller
    @Test
    public void shouldReturnedAllDStreams() {
        List<StreamDTO> list = new ArrayList<>();
    }

    public StreamDTO createStreamDTO(String name, String disciplineName) {
        return StreamDTO.builder()
                .id(1L)
                .name(name)
                .disciplineName(disciplineName)
                .build();
    }
}
