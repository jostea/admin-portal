package com.internship.adminpanel.service;

import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {

    private final StreamRepository streamRepository;

    public ResponseEntity<StreamDTO> findById(Long id) {
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent())
            return new ResponseEntity<>(new StreamDTO(stream.get()), HttpStatus.OK);

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<StreamDTO>> findAll() {
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findAll()) {
            streamDTOList.add(new StreamDTO(val));
        }
        return new ResponseEntity<>(streamDTOList, HttpStatus.OK);
    }
}
