package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.dto.discipline.DisciplineListDTO;
import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.model.dto.discipline.DisciplineDTO;
import com.internship.adminpanel.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<DisciplineListDTO> getAll(){
        ArrayList<DisciplineListDTO> disciplinesDto = new ArrayList<DisciplineListDTO>();
        ArrayList<Discipline> disciplines = new ArrayList<Discipline>();
        disciplines = (ArrayList<Discipline>) disciplineRepository.findAll();
        for (Discipline discipline : disciplines) {
            DisciplineListDTO disciplineDTO = new DisciplineListDTO();
            disciplineDTO.setId(discipline.getId());
            disciplineDTO.setName(discipline.getName());
            disciplinesDto.add(disciplineDTO);
        }
        return disciplinesDto;
    }

    public DisciplineDTO findById(Long id) throws DisciplineNotFound {
        if (disciplineRepository.findById(id).isPresent())
            return new DisciplineDTO(disciplineRepository.findById(id).get());
        throw new DisciplineNotFound(id + "");
    }

    public List<DisciplineDTO> getAllDisciplines()throws Exception {
        List<DisciplineDTO> disciplines = new ArrayList<>();
        for (Discipline val : disciplineRepository.findAll()) {
            disciplines.add(new DisciplineDTO(val));
        }
        return disciplines;
    }

    public List<DisciplineDTO> filterByName(String name) throws DisciplineNotFound {
        List<DisciplineDTO> disciplineDTOList = new ArrayList<>();
        for (Discipline val : disciplineRepository.findDisciplineByNameContainingIgnoreCase(name)) {
            disciplineDTOList.add(new DisciplineDTO(val));
        }
        return disciplineDTOList;
    }

    public void delete(Long id)throws Exception {
            disciplineRepository.deleteById(id);
    }

    public void edit(Long id, DisciplineDTO disciplineDTO, String username) throws DisciplineNotFound, EmptyName {
        Optional<Discipline> disciplineOptional = disciplineRepository.findById(id);
        if (disciplineOptional.isPresent()) {
            String oldDisciplineName = disciplineOptional.get().getName();
            Discipline discipline = disciplineOptional.get();
            if (disciplineDTO.getName().trim().isEmpty()) {
                throw new EmptyName();
            }
            discipline.setName(disciplineDTO.getName());
            disciplineRepository.save(discipline);
            log.info("Discipline '" + oldDisciplineName + "' has been changed to '" + disciplineDTO.getName()
                    + "' by user '" + username + "'");
        } else {
            throw new DisciplineNotFound(disciplineDTO.getId() + " ");
        }
    }

    public void add(DisciplineDTO disciplineDTO) throws EmptyName, Exception {
        if (disciplineDTO.getName().isEmpty())
            throw new EmptyName();
        disciplineRepository.save(Discipline.builder()
                .name(disciplineDTO.getName())
                .build());
    }
}
