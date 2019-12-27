package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.code_task.CodeTaskSubmitDTO;
import com.internship.adminpanel.model.dto.task.*;
import com.internship.adminpanel.service.CodeTaskService;
import com.internship.adminpanel.service.AssetsStorageService;
import com.internship.adminpanel.service.SqlGroupService;
import com.internship.adminpanel.service.SqlTaskService;
import com.internship.adminpanel.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasksrest")
public class TaskRestController {

    private final TaskService taskService;
    private final SqlTaskService sqlTaskService;
    private final SqlGroupService sqlGroupService;
    private final AssetsStorageService assetsStorageService;
    private final CodeTaskService codeTaskService;

    @GetMapping("/all")
    public ResponseEntity<List<TaskListDTO>> getAllTasks() {
        ArrayList<TaskListDTO> list;
        list = (ArrayList<TaskListDTO>) taskService.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/allSql")
    public ResponseEntity<List<SqlTaskListDTO>> getAllSqlTasks() {
        ArrayList<SqlTaskListDTO> list;
        list = (ArrayList<SqlTaskListDTO>) sqlTaskService.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/allCode")
    public ResponseEntity<?> getAllCodeTasks() {
        try {
            List<CodeTaskDTO> codeTasks = codeTaskService.getAll();
            return new ResponseEntity<>(codeTasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Tasks could not be loaded", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/singleCode/{id}")
    public ResponseEntity<?> getOneCodeTask(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(codeTaskService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not get the task", HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<String> saveSqlTask(@RequestBody SqlTaskInsertDTO sqlTaskInsertDTO, Authentication authentication) {
        try {
            sqlTaskService.addSqlTask(sqlTaskInsertDTO);
            log.info("[User: " + authentication.getName() + "] created new SQL task: " + sqlTaskInsertDTO.toString());
            return new ResponseEntity<>("New SQL task was created.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("[User: " + authentication.getName() + "]. Error while trying to add new SQ: task: " + sqlTaskInsertDTO.toString() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>("Error while creating new SQL task", HttpStatus.BAD_REQUEST);
        }
    }

    //region EDIT_TASK
    @GetMapping("/editTask/{id}")
    public ResponseEntity<TaskEditDTO> editTask(@PathVariable("id") Long id, Authentication authentication) {
        try {
            log.info("[User: " + authentication.getName() + "] requested a General Task to edit. Task with ID  " + id);
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
            log.error("[User: " + authentication.getName() + "]. Error while trying to edit General Task with ID: " + task.getId() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    //endregion

    //region EDIT_SQL_TASK
    @GetMapping("/editSqlTask/{id}")
    public ResponseEntity<SqlTaskEditDTO> editSqlTask(@PathVariable("id") Long id, Authentication authentication) {
        try {
            log.info("[User: " + authentication.getName() + "] requested a SQL Task to edit. Task with ID  " + id);
            return new ResponseEntity<>(sqlTaskService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            log.warn("Error while getting task with id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editSqlTask")
    public ResponseEntity<String> editTask(@RequestBody SqlTaskEditDTO sqlTaskEditDTO, Authentication authentication) {
        try{
            sqlTaskService.editTask(sqlTaskEditDTO);
            log.info("[User: " + authentication.getName() + "] modified task with ID  " + sqlTaskEditDTO.getId());
            return new ResponseEntity<>("SQL Task " + sqlTaskEditDTO.getTitle() + " was successfully modified.", HttpStatus.OK);
        } catch (Exception e){
            log.error("[User: " + authentication.getName() + "]. Error while trying to edit SQL Task with ID: " + sqlTaskEditDTO.getId() + "\nMessage: " + e.getMessage() + "; \nStack Trace: " + e.getStackTrace());
            return new ResponseEntity<>("Error while modifying SQL task", HttpStatus.BAD_REQUEST);
        }
    }
    //endregion

    @GetMapping("/editCodeTask/{id}")
    public ResponseEntity<?> editCodeTask(@PathVariable("id") Long id, @RequestBody CodeTaskSubmitDTO codeTaskSubmitDTO) {
        try {
            codeTaskService.editTask(id, codeTaskSubmitDTO);
            return new ResponseEntity<>("Successfully updated the task", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Task could not be updated", HttpStatus.BAD_REQUEST);
        }
    }

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
            log.info("[User: " + authentication.getName() + "]" + action + "Standard Task with ID  " + taskDisableDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("[User: " + authentication.getName() + "]. Error while trying to enable/disable Standard Task with ID: " + taskDisableDTO.getId() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/disableSqlTask")
    public ResponseEntity<SqlTaskEditDTO> disableSqlTask(@RequestBody SqlTaskDisableDTO sqlTaskDisableDTO, Authentication authentication) {
        try {
            sqlTaskService.disableTask(sqlTaskDisableDTO);
            String action = "";
            if (!sqlTaskDisableDTO.isEnabled()) {
                action = " disabled ";
            } else {
                action = " enabled ";
            }
            log.info("[User: " + authentication.getName() + "]" + action + "SQL Task with ID  " + sqlTaskDisableDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("[User: " + authentication.getName() + "]. Error while trying to enable/disable SQL Task with ID: " + sqlTaskDisableDTO.getId() + "; Stack Trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "imageDownload/{idSqlGroup}")
    public ResponseEntity downloadFile(@PathVariable Long idSqlGroup, HttpServletRequest request) throws FileNotFoundException {
        String urlFromDb = sqlGroupService.findById(idSqlGroup).getImagePath();
        Resource resource = assetsStorageService.loadAsResource(urlFromDb);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getURL().getFile());
        } catch (IOException ex) {
            log.error("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-streams";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @GetMapping("/streamTypeComplexityTasks")
    public ResponseEntity<Integer> getStreamTypeComplexityTasks(@RequestParam Long streamId, @RequestParam String taskType, @RequestParam String complexity){
        try{
            int number = taskService.nrStreamTasksByTypeAndComplexity(streamId,taskType,complexity);
            return new ResponseEntity<>(number, HttpStatus.OK);
        } catch (Exception e){
            log.warn("Error while getting stream Nr of tasks for stream id = " + streamId + ", taskType = " + taskType + ", complexity = " + complexity);
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addCodeTask")
    public ResponseEntity<String> addCodeTask(@RequestBody CodeTaskSubmitDTO codeTaskSubmitDTO) {
        try {
            codeTaskService.saveTask(codeTaskSubmitDTO);
            return new ResponseEntity<>("Task added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
