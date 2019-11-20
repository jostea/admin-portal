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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/streams")
    public ResponseEntity<List<StreamDTO>> streams(Authentication authentication) {
        try {
            return new ResponseEntity<>(streamService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "'  view all streams; error message:"
                    + e.getMessage() + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/streams/delete/{id}")
    public ResponseEntity<HttpStatus> deletedById(@PathVariable("id") Long id, Authentication authentication) {
        try {
            streamService.deleteById(id);
            log.info("User '" + authentication.getName() + "' deleted stream with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "'  delete stream; error message:"
                    + e.getMessage() + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/streams/name/{name}")
    public ResponseEntity<List<StreamDTO>> findByName(@PathVariable("name") String name,
                                                      Authentication authentication) {
        try {
            return new ResponseEntity<>(streamService.filterByName(name), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error while user '" + authentication.getName() + "' find stream by name '"
                    + name + "'; error message: " + e.getMessage() + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Error while user '" + authentication.getName() + "' find stream by name '"
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

    @ResponseBody
    @PutMapping("/stream/edit/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") Long id, @RequestBody StreamDTOFromUI streamDTOFromUI,
                                             Authentication authentication) {
        try {
            streamService.edit(id, streamDTOFromUI);
            log.info("User" + authentication.getName() + "' edit stream with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DisciplineNotFound | StreamNotFound | SQLException e) {
            log.error("Error when user '" + authentication.getName() + "' edit stream; error message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping("/stream/add")
    public ResponseEntity<HttpStatus> addStream(@RequestBody StreamDTOFromUI streamDTOFromUI, Authentication authentication) {
        try {
            streamService.addStream(streamDTOFromUI);
            log.info("User '" + authentication.getName() + "' add new stream '" + streamDTOFromUI.getName() + "'");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DisciplineNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' add new stream; error message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            log.error("Error when user '" + authentication.getName() + "' add new stream that already exist; error message:"
                    + e.getMessage() + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
