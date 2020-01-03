package com.internship.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestReportsController {

    @GetMapping("reportsView")
    public ModelAndView viewTestReportPage() {
        return new ModelAndView("reportsView");
    }

    @GetMapping("candidatesReport/{candidate_id}")
    public ModelAndView viewCandidatesProfile() {
        return new ModelAndView("candidatesReportView");
    }
}
