package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.users.UserDTO;
import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import com.internship.adminpanel.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/userView")
@AllArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping("/add")
    public ModelAndView addNewUserView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users/addUser");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addNewUser(@RequestBody UserDTOFromUI userDTOFromUI) {
        userService.saveUser(userDTOFromUI);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users/userView");
        return modelAndView;
    }

    @GetMapping("/users")
    public List<UserDTO> viewAll() {
        return userService.getAll();
    }
}
