package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.model.dto.discipline.DisciplineDTO;
import com.internship.adminpanel.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
            log.error("Error when user '" + authentication.getName() + "' view all  disciplines;", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody DisciplineDTO disciplineDTO, Authentication authentication) {
        try {
            disciplineService.add(disciplineDTO);
            log.info("User '" + authentication.getName() + "' add new discipline '" + disciplineDTO.getName() + "'");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyName e) {
            log.error("Error when user '" + authentication.getName() + "' add  discipline with empty name;", e);
            return new ResponseEntity<>("Discipline name is required", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            log.error("Error when user '" + authentication.getName() + "' add  discipline; error message: ", e);
            return new ResponseEntity<>("Discipline '" + disciplineDTO.getName() + "' already exist", HttpStatus.BAD_REQUEST);
        } catch (TransactionSystemException e) {
            log.error("Error when user '" + authentication.getName() + "' add new  discipline;", e);
            return new ResponseEntity<>("The specified discipline cannot be applied.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' add  discipline;", e);
            return new ResponseEntity<>("The specified discipline cannot be applied.", HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<String> edit(@PathVariable("id") Long id, @Valid @RequestBody DisciplineDTO disciplineDTO,
                                       Authentication authentication) {
        try {
            disciplineService.edit(id, disciplineDTO, authentication.getName());
            log.info("User '" + authentication.getName() + "' edit  discipline '" + disciplineDTO.getName() + "'");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyName e) {
            log.error("Error when user '" + authentication.getName() + "' update discipline with empty name;", e);
            return new ResponseEntity<>("Discipline name is required", HttpStatus.BAD_REQUEST);
        } catch (DisciplineNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' edit  discipline with id '" + id, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (TransactionSystemException e) {
            log.error("Error when user '" + authentication.getName() + "' add new  discipline; \nerror message: ", e);
            return new ResponseEntity<>("The specified discipline cannot be applied.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, Authentication authentication) {
        try {
            disciplineService.delete(id);
            log.info("User '" + authentication.getName() + "' deleted discipline with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' delete  discipline with id '" + id, e);
            return new ResponseEntity<>("Discipline has streams",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filterByName/{name}")
    public ResponseEntity<List<DisciplineDTO>> findByCharacters(@PathVariable("name") String name,
                                                                Authentication authentication) {
        try {
            return new ResponseEntity<>(disciplineService.filterByName(name), HttpStatus.OK);
        } catch (DisciplineNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' try to find stream by name '" + name + "';", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
