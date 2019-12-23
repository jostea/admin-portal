package com.internship.adminpanel.service;

import com.internship.adminpanel.model.User;
import com.internship.adminpanel.model.dto.profile.ProfileDTO;
import com.internship.adminpanel.model.dto.profile.ProfileDTOFromUI;
import com.internship.adminpanel.model.dto.users.UserDTO;
import com.internship.adminpanel.model.enums.RoleEnum;
import com.internship.adminpanel.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldFindAllUsers() {
        List<UserDTO> expectedUsers = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("test1Username");
        user1.setActive(true);
        user1.setEmail("test1mail@mail.com");
        user1.setRole(RoleEnum.ADMIN);
        User user2 = new User();
        user2.setUsername("test2Username");
        user2.setActive(true);
        user2.setEmail("test2mail@mail.com");
        user2.setRole(RoleEnum.ADMIN);
        expectedUsers.add(new UserDTO(user1));
        expectedUsers.add(new UserDTO(user2));
        List<User> mockedUsers = new ArrayList<>();
        mockedUsers.add(user1);
        mockedUsers.add(user2);
        when(userRepository.findAll()).thenReturn(mockedUsers);
        List<UserDTO> returnedUsers = userService.getAll();
        assertThat(returnedUsers).isEqualTo(expectedUsers);
    }

    @Test
    public void shouldFindUserById() {
        Long id = 1L;
        User testUser = new User();
        testUser.setUsername("testfindbyid");
        testUser.setEmail("findbyid@mail.com");
        testUser.setRole(RoleEnum.ADMIN);
        testUser.setActive(true);
        UserDTO expectedUserDTO = new UserDTO(testUser);
        when(userRepository.findById(id)).thenReturn(Optional.of(testUser));
        UserDTO returnedUser = userService.findById(id);
        verify(userRepository).findById(id);
        assertThat(expectedUserDTO).isEqualTo(returnedUser);
    }

    @Test
    public void shouldDisableUser() {
        Long id1 = 1L;
        Long id2 = 2L;

        User testUser1 = new User();
        testUser1.setUsername("test1User");
        testUser1.setActive(true);
        testUser1.setRole(RoleEnum.ADMIN);
        testUser1.setEmail("test1@mail.com");

        User testUser2 = new User();
        testUser2.setUsername("test2User");
        testUser2.setActive(false);
        testUser2.setRole(RoleEnum.ADMIN);
        testUser2.setEmail("test2@mail.com");

        when(userRepository.findById(id1)).thenReturn(Optional.of(testUser1));
        when(userRepository.findById(id2)).thenReturn(Optional.of(testUser2));
        userService.disable(id1);
        userService.disable(id2);
        verify(userRepository).findById(id1);
        verify(userRepository).save(testUser1);
    }

    @Test
    public void shouldFindUserByUsername() {
        String username = "testUsername";
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setActive(true);
        testUser.setEmail("test@mail.com");
        testUser.setRole(RoleEnum.ADMIN);
        ProfileDTO expectedUser = new ProfileDTO(testUser);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        ProfileDTO returnedUser = userService.findByUsername(username);
        assertThat(expectedUser).isEqualTo(returnedUser);
    }

    @Test
    public void shouldChangePassword() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ProfileDTOFromUI profileDTO = new ProfileDTOFromUI("password", "newpassword");
        User testUser = new User();
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole(RoleEnum.ADMIN);
        testUser.setEmail("mail@mail.com");
        testUser.setActive(true);
        testUser.setUsername("testUsername");
        when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));
        userService.changePassword("testUsername", profileDTO);
        verify(userRepository).findByUsername("testUsername");
    }
}
