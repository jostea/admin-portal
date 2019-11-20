package com.internship.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DisciplineController {

    @GetMapping("disciplineView")
    public String disciplines(){
        return "disciplineView";
    }
}
