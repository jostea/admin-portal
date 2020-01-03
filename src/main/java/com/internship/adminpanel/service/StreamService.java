package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.*;
import com.internship.adminpanel.model.*;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTOFromUI;
import com.internship.adminpanel.repository.*;
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

    private final InternshipRepository internshipRepository;

    private final StreamTimeRepository streamTimeRepository;

    private final TestStructureRepository testStructureRepository;

    private final CandidateRepository candidateRepository;

    public StreamDTO findById(Long id) throws StreamNotFound {
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent())
            return new StreamDTO(stream.get());
        throw new StreamNotFound(id + "");
    }

    public List<StreamDTO> findAll() throws Exception {
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findAll()) {
            streamDTOList.add(new StreamDTO(val));
        }
        return streamDTOList;
    }

    public List<StreamDTO> findInternshipStreams() {
        Optional<Internship> internship = internshipRepository.findFirstByIsCurrentTrue();
        List<StreamDTO> streamsInternship = new ArrayList<>();
        if (internship.isPresent()){
            for (Stream val : internship.get().getStreams()) {
                streamsInternship.add(new StreamDTO(val));
            }
        }
        return streamsInternship;
    }

    public void deleteById(Long id) throws StreamNotFound, StreamHasCandidate {
        Optional<Stream> streamOptional = streamRepository.findById(id);
        if (streamOptional.isPresent()) {
            Stream stream = streamOptional.get();
            List<Candidate> candidates = candidateRepository.findCandidateByStream(stream);
            if (candidates.size() > 0) {
                throw new StreamHasCandidate(id + "");
            }
            startDeleteCascade(stream, id);
        } else {
            throw new StreamNotFound(id + "");
        }
    }

    private void startDeleteCascade(Stream stream, Long id) {
        List<Internship> internships = internshipRepository.findAll();
        if (internships.size() > 0) {
            for (Internship internship1 : internships) {
                internship1.getStreams().remove(stream);
            }
        }
        if (stream.getTasks().size() > 0) {
            for (Task task : stream.getTasks()) {
                List<Stream> stream1 = task.getStreams();
                stream1.remove(stream);
            }
        }
        if (stream.getCodeTasks().size() > 0) {
            for (CodeTask task : stream.getCodeTasks()) {
                List<Stream> stream1 = task.getStreams();
                stream1.remove(stream);
            }
        }
        if (stream.getSqlTasks().size() > 0) {
            for (SqlTask task : stream.getSqlTasks()) {
                List<Stream> stream1 = task.getStreams();
                stream1.remove(stream);
            }
        }
        if (stream.getSkill().size() > 0) {
            for (Skill skill : stream.getSkill()) {
                List<Stream> stream1 = skill.getStreams();
                stream1.remove(stream);
            }
        }
        if (stream.getTestStructures().size() > 0) {
            for (TestStructure ts : stream.getTestStructures()) {
                testStructureRepository.deleteById(ts.getId());
            }
        }
        if (stream.getStreamTime() != null) {
            streamTimeRepository.deleteById(stream.getStreamTime().getId());
        }
        streamRepository.deleteById(id);
    }

    public List<StreamDTO> filterByName(String name) throws StreamNotFound, Exception {
        List<StreamDTO> streamDTOList = new ArrayList<>();
        for (Stream val : streamRepository.findStreamByNameContainingIgnoreCase(name)) {
            streamDTOList.add(new StreamDTO(val));
        }
        if (streamDTOList.size() == 0)
            throw new StreamNotFound(name);
        return streamDTOList;
    }

    public void addStream(StreamDTOFromUI streamUI) throws DisciplineNotFound, EmptyName {
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
            throw new StreamNotFound(id + "");
        }
    }
}
