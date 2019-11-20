package com.internship.adminpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @GetMapping("/users")
    public String users() {
        return "users/userView";
    }

    @GetMapping("/users/add")
    public ModelAndView addNewUserView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users/addUser");
        return modelAndView;
    }
}
