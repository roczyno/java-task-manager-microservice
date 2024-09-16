package com.roczyno.taskservice.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;
@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(TaskStatus.values())
                .filter(taskStatus -> taskStatus.name().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TaskStatus: " + dbData));
    }
}
