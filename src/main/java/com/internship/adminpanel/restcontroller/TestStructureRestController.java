package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.exception.TestStructureNotFound;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTO;
import com.internship.adminpanel.model.dto.teststructure.TestStructureDTOFromUI;
import com.internship.adminpanel.service.TestStructureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/testStructure/{stream_id}")
public class TestStructureRestController {

    private final TestStructureService testStructureService;

    @GetMapping("/test/{test_id}")
    public ResponseEntity<TestStructureDTO> findById(@PathVariable("test_id") Long id, Authentication authentication) {
        try {
            return new ResponseEntity<>(testStructureService.findById(id), HttpStatus.OK);
        } catch (TestStructureNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' found test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tests")
    public ResponseEntity<List<TestStructureDTO>> findAll(Authentication authentication) {
        try {
            return new ResponseEntity<>(testStructureService.findAll(), HttpStatus.OK);
        } catch (TestStructureNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' found test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody TestStructureDTOFromUI testStructureDTOFromUI,
                                      Authentication authentication) {
        try {
            testStructureService.add(testStructureDTOFromUI);
            log.info("User '" + authentication.getName() + "' add new test structure");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' add  test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()) + "\nName of Exception: " + e.getClass().getName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            log.error("Error when user '" + authentication.getName() + "' add  test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()) + "\nName of Exception: " + e.getClass().getName());
            return new ResponseEntity<>("Test structure already exist", HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PutMapping("/update/{test_id}")
    public ResponseEntity<String> update(@PathVariable("test_id") Long id, @RequestBody TestStructureDTOFromUI testStructureDTOFromUI,
                                         Authentication authentication) {
        try {
            testStructureService.update(id, testStructureDTOFromUI);
            log.info("User '" + authentication.getName() + "' updated test structure");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StreamNotFound | TestStructureNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' updated  test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()) + "\nName of Exception: " + e.getClass().getName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            log.error("Error when user '" + authentication.getName() + "' update  test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()) + "\nName of Exception: " + e.getClass().getName());
            return new ResponseEntity<>("Test structure already exist", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{test_id}")
    public ResponseEntity<String> delete(@PathVariable("test_id") Long id, Authentication authentication) {
        try {
            testStructureService.delete(id);
            log.info("User '" + authentication.getName() + "' deleted test structure");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TestStructureNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' deleted test structure; error message: " + e.getMessage()
                    + "\nstack trace: " + Arrays.toString(e.getStackTrace()) + "\nName of Exception: " + e.getClass().getName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
