package com.internship.adminpanel.controller;

import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.discipline.DisciplineListDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TypeEnum;
import com.internship.adminpanel.service.DisciplineService;
import com.internship.adminpanel.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final DisciplineService disciplineService;
    private final StreamService streamService;

    @GetMapping(value = "/")
    public String tasks() {
        return "/tasks/tasks";
    }

    @GetMapping(value = "/addTask")
    public ModelAndView addTask() {
        ModelAndView model = new ModelAndView("/tasks/addTask");

        //collection for complexitites dropdown
        List<ComplexityEnum> complexities = new ArrayList<ComplexityEnum>();
        complexities = Arrays.asList(ComplexityEnum.values());
        model.addObject("complexities",complexities);

        //collection for task-types dropdown
        List<TypeEnum> taskTypes = new ArrayList<TypeEnum>();
        taskTypes = Arrays.asList(TypeEnum.values());
        model.addObject("task_types", taskTypes);

        //collection of disciplines
        List<DisciplineListDTO> disciplinesDTO = new ArrayList<>();
        disciplinesDTO = disciplineService.getAll();    //TODO: modify to call new get All, which returns List<DisciplineDTO>
        model.addObject("disciplines", disciplinesDTO);

        //collection of streams
        List<StreamDTO> streams  = new ArrayList<>();
        streams = streamService.findAll();
        model.addObject("streams", streams);

        return model;
    }

    @GetMapping(value = "/editTask/{id}")
    public ModelAndView editTask(){
        ModelAndView model = new ModelAndView("/tasks/editTask");

        //collection for complexitites dropdown
        List<ComplexityEnum> complexities = new ArrayList<ComplexityEnum>();
        complexities = Arrays.asList(ComplexityEnum.values());
        model.addObject("complexities",complexities);

        //collection for task-types dropdown
        List<TypeEnum> taskTypes = new ArrayList<TypeEnum>();
        taskTypes = Arrays.asList(TypeEnum.values());
        model.addObject("task_types", taskTypes);

        //collection of disciplines
        List<DisciplineListDTO> disciplinesDTO = new ArrayList<>();
        disciplinesDTO = disciplineService.getAll();    //TODO: modify to call new get All, which returns List<DisciplineDTO>
        model.addObject("disciplines", disciplinesDTO);

        //collection of streams
        List<StreamDTO> streams  = new ArrayList<>();
        streams = streamService.findAll();
        model.addObject("streams", streams);

        return model;
    }
}
