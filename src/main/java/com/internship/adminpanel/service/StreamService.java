package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.exception.StreamHasTasks;
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
        throw new StreamNotFound(id + "");
    }

    public List<StreamDTO> findAll()throws Exception{
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findAll()) {
            streamDTOList.add(new StreamDTO(val));
        }
        return streamDTOList;
    }

    public void deleteById(Long id) throws StreamHasTasks, StreamNotFound {
        Optional<Stream> streamOptional = streamRepository.findById(id);
        if (streamOptional.isPresent()) {
            if (streamOptional.get().getTasks().size() > 0) {
                throw new StreamHasTasks(streamOptional.get().getName());
            }
        } else throw new StreamNotFound(id + "");
        streamRepository.deleteById(id);
    }

    public List<StreamDTO> filterByName(String name) throws StreamNotFound,Exception {
        //Convert To StreamDTO Listed
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findStreamByNameContainingIgnoreCase(name)) {
            streamDTOList.add(new StreamDTO(val));
        }
        if (streamDTOList.size() == 0)
            throw new StreamNotFound(name);
        return streamDTOList;
    }

    public void addStream(StreamDTOFromUI streamUI) throws DisciplineNotFound, EmptyName, Exception {
        Optional<Discipline> disciplineOp = disciplineRepository.findById(streamUI.getDisciplineId());
        Discipline discipline;
        if (disciplineOp.isPresent()) {
            discipline = disciplineOp.get();
        } else {
            throw new DisciplineNotFound(streamUI.getDisciplineId() + "");
        }
        if (streamUI.getName().trim().isEmpty()) {
            throw new EmptyName();
        }
        streamRepository.save(
                Stream.builder()
                .name(streamUI.getName())
                        .discipline(discipline)
                        .build());
    }

    public void edit(Long id, StreamDTOFromUI streamUI, String nameOfUser) throws SQLException, DisciplineNotFound, StreamNotFound, EmptyName {
        Optional<Stream> streamOp = streamRepository.findById(id);
        Optional<Discipline> disciplineOptional = disciplineRepository.findById(streamUI.getDisciplineId());
        Discipline discipline;
        if (disciplineOptional.isPresent()) {
            discipline = disciplineOptional.get();

        } else {
            throw new DisciplineNotFound(streamUI.getDisciplineId() + "");
        }
        if (streamOp.isPresent()) {
            String oldDisciplineName = streamOp.get().getDiscipline().getName();
            String oldName = streamOp.get().getName();
            Stream stream = streamOp.get();
            if (streamUI.getName().trim().isEmpty()) {
                throw new EmptyName();
            }
            stream.setName(streamUI.getName());
            stream.setDiscipline(discipline);
            streamRepository.save(stream);
            log.info("Stream '" + oldName + "' has changed name to '" + streamUI.getName() + "'"
                    + "and Discipline from '" + oldDisciplineName + "' to '" + discipline.getName()
                    + "' by user '" + nameOfUser + "'");
        } else {
            throw new StreamNotFound( id + "");
        }
    }
}
