package com.internship.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SkillController {

    @GetMapping("skillsView")
    public String skills() {
        return "skillsView";
    }
}
