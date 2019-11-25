package com.internship.adminpanel.model.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTOFromUI {
    private String oldPassword;
    private String newPassword;
}
