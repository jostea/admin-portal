package com.internship.adminpanel.model.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOFromUIUpdate {
    private Long id;
    private String username;
    private String email;
    private String password;
}
