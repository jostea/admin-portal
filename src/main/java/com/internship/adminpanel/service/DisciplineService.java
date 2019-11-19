package com.internship.adminpanel.service;

import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.dto.discipline.DisciplineListDTO;
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

}
