package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.EmptyListOfStreams;
import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/streams")
    public ResponseEntity<List<StreamDTO>> streams() {
        try {
            if (streamService.findAll().size() == 0)
                throw new EmptyListOfStreams("No streams found");
            return new ResponseEntity<>(streamService.findAll(), HttpStatus.OK);
        } catch (EmptyListOfStreams emptyListOfStreams) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/streams/delete/{id}")
    public void deletedById(@PathVariable("id") Long id) {
        streamService.deleteById(id);
    }


    @GetMapping("/streams/name/{name}")
    public ResponseEntity<List<StreamDTO>> findByName(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(streamService.filterByName(name), HttpStatus.OK);
        } catch (Exception streamNotFound) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }
}
