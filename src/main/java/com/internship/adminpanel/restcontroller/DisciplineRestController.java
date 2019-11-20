package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.discipline.DisciplineDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

}
