package com.internship.adminpanel.model.enums;

public enum ComplexityEnum {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private String complexity;

    ComplexityEnum(String complexity) {
        this.complexity = complexity;
    }

    public static ComplexityEnum fromString(String par) {
        for (ComplexityEnum val : ComplexityEnum.values()) {
            if (val.complexity.equalsIgnoreCase(par)) {
                return val;
            }
        }
        return null;
    }
}
