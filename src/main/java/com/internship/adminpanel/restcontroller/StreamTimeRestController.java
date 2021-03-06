package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.exception.StreamNotFound;
import com.internship.adminpanel.model.dto.stream.StreamTimeDTO;
import com.internship.adminpanel.service.StreamTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/testStructure/{stream_id}")
public class StreamTimeRestController {

    private final StreamTimeService streamTimeService;

    @GetMapping("/streamtime/{stream_id}")
    public ResponseEntity<StreamTimeDTO> getById(@PathVariable("stream_id") Long id, Authentication authentication) {
        try {
            log.info("User '" + authentication.getName() + "' call stream with id '" + id + "'");
            return new ResponseEntity<>(streamTimeService.findTimeByStreamId(id), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' find stream with id '" + id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseBody
    @PutMapping("/setTime/{stream_id}")
    public ResponseEntity<String> setTimeForStreamTest(@PathVariable("stream_id") Long id, @RequestBody Integer time, Authentication authentication) {
        try {
            log.info("User '" + authentication.getName() + "' set time " + time + " for stream's test structure with id '" + id + "'");
            streamTimeService.setTimeForStreamTest(id, time);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StreamNotFound e) {
                    log.error("Error when user '" + authentication.getName() + "' update stream with id '" + id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Error when user '" + authentication.getName() + "' update stream with id '" + id, e);
            return new ResponseEntity<>("Time must be higher then 0", HttpStatus.BAD_REQUEST);
        }
    }
}
