package com.internship.adminpanel.model.enums;

import javax.persistence.AttributeConverter;

public class ConverterSkills implements AttributeConverter<SkillsTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(SkillsTypeEnum attribute) {
        switch (attribute) {
            case TECHNICAL:
                return "Technical";
            case TOOL:
                return "Tool";
            case SOFT:
                return "Soft";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public SkillsTypeEnum convertToEntityAttribute(String dbData) {
        switch (dbData.trim()) {
            case "Technical":
                return SkillsTypeEnum.TECHNICAL;
            case "Tool":
                return SkillsTypeEnum.TOOL;
            case "Soft":
                return SkillsTypeEnum.SOFT;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}
