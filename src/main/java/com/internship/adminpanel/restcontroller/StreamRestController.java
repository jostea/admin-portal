package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.dto.stream.StreamDTO;
import com.internship.adminpanel.model.dto.stream.StreamDTOFromUI;
import com.internship.adminpanel.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/streamView")
public class StreamRestController {

    private final StreamService streamService;

    @GetMapping("/streams/{id}")
    public ResponseEntity<StreamDTO> getById(@PathVariable("id") Long id, Authentication authentication) {
        try {
            log.info("User '" + authentication.getName() + "' call stream with id '" + id + "'");
            return new ResponseEntity<>(streamService.findById(id), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' call stream with id '" + id + "' "
                    + e.getMessage() + "\n" + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/streams")
    public ResponseEntity<List<StreamDTO>> streams() {
        return new ResponseEntity<>(streamService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/streams/delete/{id}")
    @ResponseBody
    public void deletedById(@PathVariable("id") Long id, Authentication authentication) {
        streamService.deleteById(id);
        log.info("User '" + authentication.getName() + "' deleted stream with id " + id);
    }

    @GetMapping("/streams/name/{name}")
    public ResponseEntity<List<StreamDTO>> findByName(@PathVariable("name") String name,
                                                      Authentication authentication) {
        try {
            return new ResponseEntity<>(streamService.filterByName(name), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.warn("Error while user '" + authentication.getName() + "' getting stream by name '"
                    + name + "'; error message: " + e.getMessage() + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/streams/discipline/{discipline}")
    public ResponseEntity<List<StreamDTO>>streamsByDiscipline(@PathVariable("discipline") String discipline){
        ResponseEntity<List<StreamDTO>> responseEntity;
        try{
            List<StreamDTO> list = streamService.findAll()
                    .stream()
                    .filter(s -> s.getDisciplineName().equalsIgnoreCase(discipline))
                    .collect(Collectors.toList());
            responseEntity =  new ResponseEntity<>(list, HttpStatus.OK);
            log.info("A set of streams was returned by the discipline name: " + discipline);
        } catch (Exception e){
            responseEntity =  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            log.error("Error while getting stream by discipline name: " + discipline + "; stack trace: " + e.getStackTrace());
        }
        return responseEntity;
    }

    @PutMapping("/stream/edit/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody StreamDTOFromUI streamDTOFromUI,
                       Authentication authentication) {
        try {
            streamService.edit(id, streamDTOFromUI);
            log.info("User" + authentication.getName() + "' edit stream with id " + id);
        } catch (SQLException | DisciplineNotFound e) {
            log.info("Error when user '" + authentication.getName() + "' edit stream; error message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
        }
    }

    @ResponseBody
    @PostMapping("/stream/add")
    public void addStream(@RequestBody StreamDTOFromUI streamDTOFromUI, Authentication authentication) {
        try {
            streamService.addStream(streamDTOFromUI);
            log.info("User '" + authentication.getName() + "' add new stream '" + streamDTOFromUI.getName() + "'");
        } catch (SQLException | DisciplineNotFound e) {
            log.info("Error when user '" + authentication.getName() + "' add new user; error message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
        }
    }
}
