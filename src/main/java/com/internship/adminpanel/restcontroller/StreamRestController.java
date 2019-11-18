package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/streamView")
public class StreamRestController {

    private final StreamService streamService;

    @GetMapping("/streams/{id}")
    public ResponseEntity<StreamDTO> stream(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(streamService.findById(id), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error(e.getMessage());
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
        log.info("User '" + authentication.getName() + "' deleted stream with id " + id);
        streamService.deleteById(id);
    }

    @GetMapping("/streams/name/{name}")
    public ResponseEntity<List<StreamDTO>> findByName(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(streamService.filterByName(name), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/streams/discipline/{discipline}")
    public ResponseEntity<List<StreamDTO>>streamsByDiscipline(@PathVariable("discipline") String discipline){
        ResponseEntity<List<StreamDTO>> responseEntity;
        try{
            List<StreamDTO> list = streamService.findAll().stream().filter(s -> s.getDisciplineName().equalsIgnoreCase(discipline)).collect(Collectors.toList());
            responseEntity =  new ResponseEntity<>(list, HttpStatus.OK);
            log.info("A set of streams was returned by the discipline name: " + discipline);
        } catch (Exception e){
            responseEntity =  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            log.error("Errow while getting stream by discipline name: " + discipline + "; stack trace: " + e.getStackTrace());
        }
        return responseEntity;
    }
}
