package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {

    private final StreamRepository streamRepository;

    public StreamDTO findById(Long id) throws StreamNotFound {
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent())
            return new StreamDTO(stream.get());

        throw new StreamNotFound("Stream with " + id + "is not found");
    }

    public List<StreamDTO> findAll() {
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findAll()) {
            streamDTOList.add(new StreamDTO(val));
        }
        return streamDTOList;
    }

    public void deleteById(Long id) {
        streamRepository.deleteById(id);
    }

    public List<StreamDTO> filterByName(String name){

        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findAll()) {
            if (val.getName().toLowerCase().contains(name.toLowerCase())) {
                streamDTOList.add(new StreamDTO(val));
            }
        }


        return streamDTOList;
    }
}
