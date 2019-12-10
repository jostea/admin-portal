package com.internship.adminpanel.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            modelAndView.setViewName("main");
        } else {
            modelAndView.setViewName("redirect:./landing");
        }
        return modelAndView;
    }

    @GetMapping("/landing")
    public String landing() {
        return "landing";
    }

    @GetMapping("/profile")
    public ModelAndView getProfile() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        return modelAndView;
    }
}
