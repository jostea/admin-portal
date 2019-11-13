package com.internship.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StreamController {

    @GetMapping("streamView")
    public String streamView() {
        return "streamView";
    }
}
