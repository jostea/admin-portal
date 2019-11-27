package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.task.*;
import com.internship.adminpanel.service.SqlTaskService;
import com.internship.adminpanel.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasksrest")
public class TaskRestController {

    private final TaskService taskService;
    private final SqlTaskService sqlTaskService;

    @GetMapping("/all")
    public ResponseEntity<List<TaskListDTO>> getAllTasks() {
        ArrayList<TaskListDTO> list = new ArrayList<TaskListDTO>();
        list = (ArrayList<TaskListDTO>) taskService.getAll();
        return new ResponseEntity<List<TaskListDTO>>(list, HttpStatus.OK);
    }

    @GetMapping("/allSql")
    public ResponseEntity<List<SqlTaskListDTO>> getAllSqlTasks() {
        ArrayList<SqlTaskListDTO> list;
        list = (ArrayList<SqlTaskListDTO>) sqlTaskService.getAll();
        return new ResponseEntity<List<SqlTaskListDTO>>(list, HttpStatus.OK);
    }

    @PostMapping("/addTask")
    public ResponseEntity<String> saveTask(@RequestBody TaskInsertDTO task, Authentication authentication) {
        try {
            taskService.addTask(task);
            log.info("[User: " + authentication.getName() + "] created new task: " + task.toString());
            return new ResponseEntity<>("New task was created.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("[User: " + authentication.getName() + "]. Error while trying to add new task: " + task.toString() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>("Error while creating new task", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addSqlTask")
    public ResponseEntity<String> saveSqlTask(@RequestBody SqlTaskInsertDTO sqlTaskInsertDTO, Authentication authentication){
        try{
            sqlTaskService.addSqlTask(sqlTaskInsertDTO);
            log.info("[User: " + authentication.getName() + "] created new SQL task: " + sqlTaskInsertDTO.toString());
            return new ResponseEntity<>("New SQL task was created.", HttpStatus.OK);
        } catch (Exception e){
            log.error("[User: " + authentication.getName() + "]. Error while trying to add new SQ: task: " + sqlTaskInsertDTO.toString() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>("Error while creating new SQL task", HttpStatus.BAD_REQUEST);
        }
    }

    //region EDIT_TASK
    @GetMapping("/editTask/{id}")
    public ResponseEntity<TaskEditDTO> editTask(@PathVariable("id") Long id, Authentication authentication) {
        try {
            log.info("[User: " + authentication.getName() + "] requested a task to edit. Task with ID  " + id);
            return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            log.warn("Error while getting task with id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editTask")
    public ResponseEntity<String> editTask(@RequestBody TaskEditDTO task, Authentication authentication) {
        try {
            taskService.editTask(task);
            log.info("[User: " + authentication.getName() + "] modified task with ID  " + task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("[User: " + authentication.getName() + "]. Error while trying to update task with ID: " + task.getId() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    //endregion

    @GetMapping("/viewTask/{id}")
    public ResponseEntity<TaskEditDTO> viewTask(@PathVariable("id") Long id, Authentication authentication) {
        try {
            log.info("[User: " + authentication.getName() + "] viewed task with ID  " + id);
            return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            log.warn("[User: " + authentication.getName() + "]. Error while trying to view a task with ID: " + id + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/disableTask")
    public ResponseEntity<TaskEditDTO> disableTask(@RequestBody TaskDisableDTO taskDisableDTO, Authentication authentication) {
        try {
            taskService.disableTask(taskDisableDTO);
            String action = "";
            if (!taskDisableDTO.isEnabled()) {
                action = " disabled ";
            } else {
                action = " enabled ";
            }
            log.info("[User: " + authentication.getName() + "]" + action + "task with ID  " + taskDisableDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("[User: " + authentication.getName() + "]. Error while trying to enable/disable task with ID: " + taskDisableDTO.getId() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
