package com.internship.adminpanel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.internship.adminpanel.model.enums.RoleEnum;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotNull(message = "Field is required")
    @Length(max = 50, message = "Max size is 50")
    @Column(name = "userName")
    private String userName;

    @NotNull(message = "Field is required")
    @Email(message = "Please provide a valid eMail")
    @Column(name = "eMail")
    private String email;

    @NotNull(message = "Field is required")
    @Length(min = 8, message = "Min length 8")
    @Column(name = "pass")
    private String password;

    @NotNull(message = "Field is required")
    @Column(name = "isActive")
    private boolean isActive;

    @NotNull(message = "Role is required")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    RoleEnum role;
}
