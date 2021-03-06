package com.internship.adminpanel.controller;

import com.internship.adminpanel.model.dto.discipline.DisciplineListDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.task.SqlGroupDTO;
import com.internship.adminpanel.model.enums.ComplexityEnum;
import com.internship.adminpanel.model.enums.TechnologyEnum;
import com.internship.adminpanel.model.enums.TypeEnum;
import com.internship.adminpanel.service.DisciplineService;
import com.internship.adminpanel.service.SqlGroupService;
import com.internship.adminpanel.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskController {

    private final DisciplineService disciplineService;
    private final StreamService streamService;
    private final SqlGroupService sqlGroupService;

    @GetMapping(value = "/tasks")
    public String tasks() {
        return "tasks/tasksView";
    }

    @GetMapping(value = "/tasks/addTask")
    public ModelAndView addTask() {
        ModelAndView model = new ModelAndView("tasks/addTask");
        model = populateModelWithDropDowns(model);
        return model;
    }

    @GetMapping(value = "/tasks/addSqlTask")
    public ModelAndView addSqlTask() {
        ModelAndView model = new ModelAndView("tasks/addSqlTask");
        model = populateModelWithDropDowns(model);

        //add to the model SqlGroup options
        List<SqlGroupDTO> sqlGroupDTOs;
        sqlGroupDTOs = sqlGroupService.getAll();
        model.addObject("sqlgroups", sqlGroupDTOs);
        
        return model;
    }

    @GetMapping(value = "/tasks/editTask/{id}")
    public ModelAndView editTask() {
        ModelAndView model = new ModelAndView("tasks/editTask");
        model = populateModelWithDropDowns(model);

        return model;
    }

    @GetMapping(value = "/tasks/editSqlTask/{id}")
    public ModelAndView editSqlTask() {
        ModelAndView model = new ModelAndView("tasks/editSqlTask");
        model = populateModelWithDropDowns(model);

        //add to the model SqlGroup options
        List<SqlGroupDTO> sqlGroupDTOs;
        sqlGroupDTOs = sqlGroupService.getAll();
        model.addObject("sqlgroups", sqlGroupDTOs);

        return model;
    }

    @GetMapping(value = "/tasks/viewTask/{id}")
    public ModelAndView viewTask() {
        ModelAndView model = new ModelAndView("tasks/viewTask");
        model = populateModelWithDropDowns(model);
        return model;
    }

    @GetMapping(value = "/tasks/viewSqlTask/{id}")
    public ModelAndView viewSqlTask() {
        ModelAndView model = new ModelAndView("tasks/viewSqlTask");
        model = populateModelWithDropDowns(model);

        //add to the model SqlGroup options
        List<SqlGroupDTO> sqlGroupDTOs;
        sqlGroupDTOs = sqlGroupService.getAll();
        model.addObject("sqlgroups", sqlGroupDTOs);

        return model;
    }

    @GetMapping("/tasks/viewCodeTask/{id}")
    public ModelAndView viewCodeTask() {
        ModelAndView modelAndView = new ModelAndView("tasks/viewCodeTask");
        return modelAndView;
    }

    @GetMapping("/tasks/addCodeTask")
    public ModelAndView addCodeTask() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tasks/addCodeTask");
        populateModelWithDropDownsAddCodeTask(modelAndView);
        return modelAndView;
    }

    private void populateModelWithDropDownsAddCodeTask(ModelAndView modelAndView) throws Exception {
        List<ComplexityEnum> complexities;
        complexities = Arrays.asList(ComplexityEnum.values());
        modelAndView.addObject("complexities", complexities);

        List<DisciplineListDTO> disciplinesDTO;
        disciplinesDTO = disciplineService.getAll();
        modelAndView.addObject("disciplines", disciplinesDTO);

        List<StreamDTO> streams;
        streams = streamService.findAll();
        modelAndView.addObject("streams", streams);

        List<TechnologyEnum> technologies;
        technologies = Arrays.asList(TechnologyEnum.values());
        modelAndView.addObject("technologies", technologies);
    }

    @GetMapping("/tasks/editCodeTask/{id}")
    public ModelAndView editCodeTask() throws Exception {
        ModelAndView modelAndView = new ModelAndView("tasks/editCodeTask");
        populateModelWithDropDownsAddCodeTask(modelAndView);
        return modelAndView;
    }

    private ModelAndView populateModelWithDropDowns(ModelAndView model) {
        //collection for complexitites dropdown
        List<ComplexityEnum> complexities;
        complexities = Arrays.asList(ComplexityEnum.values());
        model.addObject("complexities", complexities);

        //collection for task-types dropdown
        List<TypeEnum> taskTypes;
        taskTypes = Arrays.asList(TypeEnum.values());
        model.addObject("task_types", taskTypes);

        //collection of disciplines
        List<DisciplineListDTO> disciplinesDTO;
        disciplinesDTO = disciplineService.getAll();
        model.addObject("disciplines", disciplinesDTO);

        //collection of streams
        List<StreamDTO> streams;
        try {
            streams = streamService.findAll();
            model.addObject("streams", streams);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return model;
    }
}
