package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.Stream;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTOFromUI;
import com.internship.adminpanel.repository.DisciplineRepository;
import com.internship.adminpanel.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {

    private final StreamRepository streamRepository;
    private final DisciplineRepository disciplineRepository;

    public StreamDTO findById(Long id) throws StreamNotFound {
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent())
            return new StreamDTO(stream.get());
        throw new StreamNotFound("Requested stream was not found");
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

    public List<StreamDTO> filterByName(String name) throws StreamNotFound {
        //Convert To StreamDTO Listed
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findStreamByNameContaining(name.toUpperCase())) {
            streamDTOList.add(new StreamDTO(val));
        }
        if (streamDTOList.size() == 0)
            throw new StreamNotFound("Requested stream was not found");
        return streamDTOList;
    }

    public void addStream(StreamDTOFromUI streamUI) throws SQLException, DisciplineNotFound {
        Optional<Discipline> disciplineOp = disciplineRepository.findById(streamUI.getDisciplineId());
        Discipline discipline;
        if (disciplineOp.isPresent()) {
            discipline = disciplineOp.get();
        } else {
            throw new DisciplineNotFound("Discipline with id '" + streamUI.getDisciplineId() + "' didn't found");
        }
        streamRepository.save(
                Stream.builder()
                .name(streamUI.getName())
                        .discipline(discipline)
                        .build());
    }

    public void edit(Long id, StreamDTOFromUI streamUI) throws SQLException, DisciplineNotFound {
        Optional<Stream> streamOp = streamRepository.findById(id);
        Discipline discipline ;
        if (disciplineRepository.findById(streamUI.getDisciplineId()).isPresent()) {
            discipline = disciplineRepository.findById(streamUI.getDisciplineId()).get();
        } else {
            throw new DisciplineNotFound("Discipline with id '" + streamUI.getDisciplineId() + "' didn't found");
        }
        if (streamOp.isPresent()) {
            Stream stream = streamOp.get();
            stream.setName(streamUI.getName());
            stream.setDiscipline(discipline);
            streamRepository.save(stream);
        }
    }
}
