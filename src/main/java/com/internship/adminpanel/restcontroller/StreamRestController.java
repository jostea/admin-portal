package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.service.StreamService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("streamView")
public class StreamRestController {

    private StreamService streamService;

    @GetMapping("/streams/{id}")
    public ResponseEntity<StreamDTO> stream(@PathVariable("id") Long id) {
        return streamService.findById(id);
    }

    @GetMapping("/streams/")
    public ResponseEntity<List<StreamDTO>> streams() {
        return streamService.findAll();
    }
}
