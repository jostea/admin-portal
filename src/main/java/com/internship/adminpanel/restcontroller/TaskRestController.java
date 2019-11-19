package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.task.TaskInsertDTO;
import com.internship.adminpanel.model.dto.task.TaskListDTO;
import com.internship.adminpanel.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasksrest")
public class TaskRestController {

    private final TaskService taskService;

    @GetMapping("/all")
    public ResponseEntity<List<TaskListDTO>> getAllTasks() {
        ArrayList<TaskListDTO> list = new ArrayList<TaskListDTO>();
        list = (ArrayList<TaskListDTO>) taskService.getAll();
        return new ResponseEntity<List<TaskListDTO>>(list, HttpStatus.OK);
    }

    @PostMapping("/addTask")
    public ResponseEntity<String> saveTask(@RequestBody TaskInsertDTO task) {
        try{
            taskService.addTask(task);
            log.info("New task created: " + task.toString());
            return new ResponseEntity<>("New task was created.", HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Error while trying to add new task: " + task.toString() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>("New task was created.", HttpStatus.BAD_REQUEST);
        }
    }


}
