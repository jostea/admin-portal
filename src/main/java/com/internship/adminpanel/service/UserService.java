package com.internship.adminpanel.service;

import com.internship.adminpanel.model.User;
import com.internship.adminpanel.model.dto.users.UserDTO;
import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import com.internship.adminpanel.model.enums.RoleEnum;
import com.internship.adminpanel.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDTO> getAll() {
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            userDTOS.add(new UserDTO(user));
        }

        return userDTOS;
    }

    public void saveUser(UserDTOFromUI userDTOFromUI) {
        User localUser = new User();
        localUser.setUsername(userDTOFromUI.getUsername());
        localUser.setEmail(userDTOFromUI.getEmail());
        localUser.setPassword(generatePassword());
        localUser.setRole(RoleEnum.ADMIN);
        localUser.setActive(true);
        userRepository.save(localUser);
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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordGenerator.generatePassword(8, specialCharacterRule);
        System.out.println(password);
        return passwordEncoder.encode(password);
    }
}
