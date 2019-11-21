package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.users.UserDTO;
import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import com.internship.adminpanel.model.dto.users.UserDTOFromUIUpdate;
import com.internship.adminpanel.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserRestController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> viewAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> viewUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<UserDTO> editUserResponse(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editUserPut(@RequestBody UserDTOFromUIUpdate userDTOFromUIUpdate, @PathVariable("id") Long id) {
        try {
            userService.updateById(userDTOFromUIUpdate, id);
            return new ResponseEntity<>("User edited successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.info(userDTOFromUIUpdate.getUsername() + " could not be updated. Stack Trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>("Unable to edit user", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> disable(@PathVariable("id") Long id) {
        try {
            userService.disable(id);
            return new ResponseEntity<>("User with id " + id + " has been enabled/disabled", HttpStatus.OK);
        } catch (Exception e) {
            log.warn("Unable to perform action on changing user's activity status " + "with id: " + id + ". Stack Trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>("Error while disabling the user", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTOFromUI userDTOFromUI) {
        try {
            userService.saveUser(userDTOFromUI);
            return new ResponseEntity<>("You have succesfully saved a new user ", HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error when trying to add a new user " + userDTOFromUI.getUsername() + ". Here is the Stack Trace: " + Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>("Error while trying to add a new user ", HttpStatus.BAD_REQUEST);
        }
    }
}
