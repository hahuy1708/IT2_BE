package vn.udn.dut.it2.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Persists a {@code List<String>} of tags as a JSON array in a TEXT column.
 */
@Converter
public class StringListJsonConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<String>> LIST_TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }
        String trimmed = dbData.trim();
        if (trimmed.startsWith("[")) {
            try {
                List<String> parsed = MAPPER.readValue(trimmed, LIST_TYPE);
                return parsed != null ? parsed : Collections.emptyList();
            } catch (Exception ignored) {
                // Fall through to delimiter split for manually inserted rows.
            }
        }
        List<String> fallback = new ArrayList<>();
        Arrays.stream(trimmed.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(fallback::add);
        return fallback;
    }
}
