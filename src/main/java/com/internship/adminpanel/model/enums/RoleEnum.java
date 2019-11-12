package com.internship.adminpanel.model.enums;

public enum RoleEnum {
    ADMIN("Admin"),
    SUPER_ADMIN("Super Admin");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public static RoleEnum fromString(String par) {
        for (RoleEnum val : RoleEnum.values()) {
            if (val.role.equalsIgnoreCase(par))
                return val;
        }

        return null;
    }
}
