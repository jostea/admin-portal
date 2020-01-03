package com.internship.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestStructureViewController {

    @GetMapping("/testStructure/{stream_id}")
    public String testStructure() {
        return "teststructure/testStructureView";
    }
}
