package com.internship.adminpanel.service;

import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {

    private final StreamRepository streamRepository;

    public StreamDTO findStreamById(Long id) {
        Stream stream = streamRepository.findByStreamId(id);
        log.info("{} | {} | {}", stream.getStreamName(), stream.getStreamId(), LocalDate.now());
        return new StreamDTO(stream);
    }

    public List<StreamDTO> findAll() {
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findAll()) {
            streamDTOList.add(new StreamDTO(val));
            log.info("{} | {} | {}", val.getStreamName(), val.getStreamId(),LocalDate.now());
        }
        return streamDTOList;
    }
}
