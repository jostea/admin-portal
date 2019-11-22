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
        throw new DisciplineNotFound("Requested discipline was not found");
    }

    public List<DisciplineDTO> getAllDisciplines() {
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

    public void delete(Long id) {
        try {
            disciplineRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void edit(Long id, DisciplineDTO disciplineDTO) throws DisciplineNotFound {
        Optional<Discipline> disciplineOptional = disciplineRepository.findById(id);
        if (disciplineOptional.isPresent()) {
            Discipline discipline = disciplineOptional.get();
            discipline.setName(disciplineDTO.getName());
            disciplineRepository.save(discipline);
        } else {
            throw new DisciplineNotFound("The discipline with id:" + disciplineDTO.getId() + "didn't found.");
        }
    }

    public void add(DisciplineDTO disciplineDTO) {
        disciplineRepository.save(Discipline.builder()
                .name(disciplineDTO.getName()).build());
    }
}
