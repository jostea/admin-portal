package com.internship.adminpanel.service;

import com.internship.adminpanel.model.User;
import com.internship.adminpanel.model.dto.profile.ProfileDTO;
import com.internship.adminpanel.model.dto.profile.ProfileDTOFromUI;
import com.internship.adminpanel.model.dto.users.UserDTO;
import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import com.internship.adminpanel.model.dto.users.UserDTOFromUIUpdate;
import com.internship.adminpanel.model.enums.RoleEnum;
import com.internship.adminpanel.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
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
    private final NotificationService notificationSender;

    public List<UserDTO> getAll() {
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            if (!user.getRole().equals(RoleEnum.SUPER_ADMIN))
                userDTOS.add(new UserDTO(user));
        }
        return userDTOS;
    }

    public void saveUser(UserDTOFromUI userDTOFromUI) throws Exception {
        try {
            String currentUserPass = generatePassword();
            User localUser = new User(userDTOFromUI, currentUserPass);
            userRepository.save(localUser);
            notificationSender.sendMessage(userDTOFromUI, currentUserPass);
        } catch (Exception e) {
            log.warn("Could not save user " + userDTOFromUI.getUsername() + " Stack Trace: " + Arrays.toString(e.getStackTrace()));
            throw new Exception("User could not be saved check the credentials, they may be existing or the email address could be invalid");
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
        } else {
            log.error("Unable to update " + userDTOFromUIUpdate.getUsername());
        }
    }

    public void disable(Long id) {
        Optional<User> local = userRepository.findById(id);
        if (local.isPresent()) {
            if(local.get().isActive()) {
                local.get().setActive(false);
                log.info("Main admin has disabled user  " + local.get().getUsername());
            } else {
                local.get().setActive(true);
                log.info("Main admin has enabled user  " + local.get().getUsername());
            }
            userRepository.save(local.get());
        } else {
            log.error("User with id: " + id + " could not be disabled/enabled because it is not in the system");
        }
    }

    public ProfileDTO findByUsername(String username) {
        return new ProfileDTO(userRepository.findByUsername(username).get());
    }

    public void changePassword(String username, ProfileDTOFromUI profileDTOFromUI) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User local = userRepository.findByUsername(username).get();
        if (passwordEncoder.matches(profileDTOFromUI.getOldPassword(), local.getPassword())) {
            local.setPassword(passwordEncoder.encode(profileDTOFromUI.getNewPassword()));
            userRepository.save(local);
        } else {
            throw new Exception("Old passwords don't match");
        }
    }

    private static String generatePassword() {
        CharacterRule specialCharacterRule = new CharacterRule(new CharacterData() {
            @Override
            public String getErrorCode() {
                return "SAMPLE_ERROR_CODE";
            }

            @Override
            public String getCharacters() {
                return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*";
            }
        });
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generatePassword(8, specialCharacterRule);
        return password;
    }
}
