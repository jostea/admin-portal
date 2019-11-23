package com.internship.adminpanel.restcontroller;

import com.internship.adminpanel.model.dto.profile.ProfileDTO;
import com.internship.adminpanel.model.dto.profile.ProfileDTOFromUI;
import com.internship.adminpanel.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
@Slf4j
public class ProfileRestController {
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> editPassword(@RequestBody ProfileDTOFromUI profileDTOFromUI, @PathVariable("username") String username) {
        try {
            userService.changePassword(username, profileDTOFromUI);
            return new ResponseEntity<>("Your password has been changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Old passwords don't match", HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
