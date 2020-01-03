package com.internship.adminpanel.model;

import com.internship.adminpanel.model.dto.users.UserDTOFromUI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.internship.adminpanel.model.enums.RoleEnum;
import org.hibernate.validator.constraints.Length;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Field is required")
    @Length(min = 6, max = 50, message = "Min size is 6, Max size is 50")
    private String username;

    @NotNull(message = "Field is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotNull(message = "Field is required")
    @Length(min = 8, message = "Min length 8")
    private String password;

    @NotNull(message = "Field is required")
    @Column(name = "is_active")
    private boolean isActive;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public User(UserDTOFromUI userDTOFromUI, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.setUsername(userDTOFromUI.getUsername());
        this.setEmail(userDTOFromUI.getEmail());
        this.setPassword(passwordEncoder.encode(password));
        this.setRole(RoleEnum.ADMIN);
        this.setActive(true);
    }
}
