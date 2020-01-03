package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.StreamTime;
import com.internship.adminpanel.model.dto.stream.StreamTimeDTO;
import com.internship.adminpanel.repository.StreamRepository;
import com.internship.adminpanel.repository.StreamTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamTimeService {

    private final StreamTimeRepository streamTimeRepository;

    private final StreamRepository streamRepository;

    public StreamTimeDTO findTimeByStreamId(Long streamId) throws StreamNotFound {
        Optional<Stream> optionalStream = streamRepository.findById(streamId);
        StreamTime streamTime;
        Stream stream;
        if (optionalStream.isPresent()) {
            stream = optionalStream.get();
            if (stream.getStreamTime() != null) {
                streamTime = stream.getStreamTime();
                return new StreamTimeDTO(streamTime);
            } else
                return StreamTimeDTO.builder()
                        .id(1L)
                        .time(0).build();
        }
        throw new StreamNotFound(streamId + "");
    }

    public void setTimeForStreamTest(Long streamId, Integer time) throws StreamNotFound, IllegalArgumentException {
        if (time < 0)
            throw new IllegalArgumentException("Wrong time for test");

        Optional<Stream> streamOptional = streamRepository.findById(streamId);
        Stream stream;
        if (streamOptional.isPresent()) {
            stream = streamOptional.get();
            StreamTime streamTime = stream.getStreamTime();
            if (streamTime != null) {
                streamTime.setTimeTest(time);
                streamTimeRepository.save(streamTime);
            } else {
                streamTimeRepository.save(StreamTime.builder()
                        .stream(stream)
                        .timeTest(time)
                        .build());
            }
        } else {
            throw new StreamNotFound(streamId + "");
        }
    }
}
