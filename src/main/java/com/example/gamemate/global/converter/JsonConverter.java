package com.example.gamemate.global.converter;

import com.example.gamemate.global.exception.JsonConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@Converter
public class JsonConverter implements AttributeConverter<List<Integer>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting list to JSON string: {}", attribute, e);
            throw new JsonConversionException("Error converting list to JSON string: " + attribute, e);
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            log.error("Error converting JSON string to list: {}", dbData, e);
            throw new JsonConversionException("Error converting JSON string to list: " + dbData, e);
        }
    }
}

