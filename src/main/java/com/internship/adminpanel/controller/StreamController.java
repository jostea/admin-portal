package com.internship.adminpanel.controller;

import com.internship.adminpanel.model.dto.StreamDTO;
import com.internship.adminpanel.service.StreamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/stream")
public class StreamController {

    private StreamService streamService;

    @GetMapping("/{id}")
    public StreamDTO stream(@PathVariable("id") Long id) {
        return streamService.findStreamById(id);
    }

    @GetMapping("")
    public List<StreamDTO> streams() {
        return streamService.findAll();
    }
}
