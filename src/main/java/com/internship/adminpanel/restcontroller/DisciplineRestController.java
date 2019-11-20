package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.model.dto.discipline.DisciplineDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/discipline")
public class DisciplineRestController {

    private final DisciplineService disciplineService;

    @GetMapping("/disciplines")
    public ResponseEntity<List<DisciplineDTO>> getAll() {
        return new ResponseEntity<>(disciplineService.getAllDisciplines(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/add")
    public void add(@RequestBody DisciplineDTO disciplineDTO, Authentication authentication) {
        disciplineService.add(disciplineDTO);
        log.info("User '" + authentication.getName() + "' add new discipline '" + disciplineDTO.getName() + "'");
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public void edit(@PathVariable("id") Long id, @RequestBody DisciplineDTO disciplineDTO,
                     Authentication authentication) {
        try {
            disciplineService.edit(id, disciplineDTO);
            log.info("User '" + authentication.getName() + "' edit new discipline '" + disciplineDTO.getName() + "'");
        } catch (DisciplineNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' edit  discipline with id '" + id + "'; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id, Authentication authentication) {
        disciplineService.delete(id);
        log.info("User '" + authentication.getName() + "' deleted discipline with id " + id);
    }

    @GetMapping("/filterByName/{name}")
    public ResponseEntity<List<DisciplineDTO>> findByCharacters(@PathVariable("name") String name,
                                                                Authentication authentication) {
        try {
            return new ResponseEntity<>(disciplineService.filterByName(name), HttpStatus.OK);
        } catch (DisciplineNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' try to find stream by name '" + name + "';"
                    + e.getMessage() + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
