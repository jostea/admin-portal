package com.internship.adminpanel.service;

import com.internship.adminpanel.model.User;
import com.internship.adminpanel.model.dto.profile.ProfileDTO;
import com.internship.adminpanel.model.dto.users.UserDTO;
import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import com.internship.adminpanel.model.dto.users.UserDTOFromUIUpdate;
import com.internship.adminpanel.model.enums.RoleEnum;
import com.internship.adminpanel.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public List<UserDTO> getAll() {
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            if (!user.getRole().equals(RoleEnum.SUPER_ADMIN))
                userDTOS.add(new UserDTO(user));
        }
        return userDTOS;
    }

    public void saveUser(UserDTOFromUI userDTOFromUI) {
        try {
            User localUser = new User(userDTOFromUI);
            userRepository.save(localUser);
        } catch (Exception e) {
            log.error("Could not save user " + userDTOFromUI.getUsername() + " Stack Trace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public UserDTO findById(Long id) {
        return new UserDTO(userRepository.findById(id).get());
    }

    public void updateById(UserDTOFromUIUpdate userDTOFromUIUpdate, Long id) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<User> local = userRepository.findById(id);
        if (local.isPresent()) {
            local.get().setUsername(userDTOFromUIUpdate.getUsername());
            local.get().setEmail(userDTOFromUIUpdate.getEmail());
            if (!userDTOFromUIUpdate.getPassword().isEmpty()) {
                local.get().setPassword(passwordEncoder.encode(userDTOFromUIUpdate.getPassword()));
            }
            userRepository.save(local.get());
            log.info("Main admin has updated user " + userDTOFromUIUpdate.getUsername());
        } else {
            log.error("Unable to update " + userDTOFromUIUpdate.getUsername());
        }
    }

    public void disable(Long id) {
        Optional<User> local = userRepository.findById(id);
        if (local.isPresent()) {
            if(local.get().isActive()) {
                local.get().setActive(false);
            } else {
                local.get().setActive(true);
            }
            userRepository.save(local.get());
            log.info("Main admin has disabled user  " + local.get().getUsername());
        } else {
            log.error("User with id: " + id + " could not be disabled");
        }
    }

    public ProfileDTO findByUsername(String username) {
        return new ProfileDTO(userRepository.findByUsername(username).get());
    }
}
