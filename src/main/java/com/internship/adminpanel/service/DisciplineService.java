package com.internship.adminpanel.service;

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
        throw new DisciplineNotFound("Requested discipline was not found");
    }

    public List<DisciplineDTO> getAllDisciplines() {
        List<DisciplineDTO> disciplines = new ArrayList<>();
        for (Discipline val : disciplineRepository.findAll()) {
            disciplines.add(new DisciplineDTO(val));
        }
        return disciplines;
    }

    public DisciplineDTO findByName(String name) throws DisciplineNotFound {
        if (disciplineRepository.findByName(name).isPresent())
            return new DisciplineDTO(disciplineRepository.findByName(name).get());
        else {
            throw new DisciplineNotFound("Discipline with name '" + name + "' didn't found.");
        }

    }
}
