package com.internship.adminpanel.model.dto.profile;

import com.internship.adminpanel.model.User;
import com.internship.adminpanel.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {
    private Long id;
    private String username;
    private String role;

    public ProfileDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().getRole();
    }
}
