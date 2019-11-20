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
    public ResponseEntity<List<DisciplineDTO>> getAll(Authentication authentication) {
        try {
            return new ResponseEntity<>(disciplineService.getAllDisciplines(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' view all  disciplines; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody DisciplineDTO disciplineDTO, Authentication authentication) {
        try {
            disciplineService.add(disciplineDTO);
            log.info("User '" + authentication.getName() + "' add new discipline '" + disciplineDTO.getName() + "'");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' add  discipline; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<HttpStatus> edit(@PathVariable("id") Long id, @RequestBody DisciplineDTO disciplineDTO,
                                           Authentication authentication) {
        try {
            disciplineService.edit(id, disciplineDTO);
            log.info("User '" + authentication.getName() + "' edit new discipline '" + disciplineDTO.getName() + "'");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DisciplineNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' edit  discipline with id '" + id + "'; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id, Authentication authentication) {
        try {
            disciplineService.delete(id);
            log.info("User '" + authentication.getName() + "' deleted discipline with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' delete  discipline with id '" + id + "'; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
