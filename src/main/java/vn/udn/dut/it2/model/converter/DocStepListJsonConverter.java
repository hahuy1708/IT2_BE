package vn.udn.dut.it2.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import vn.udn.dut.it2.model.dto.DocDetailDto;

import java.util.Collections;
import java.util.List;

/**
 * Persists a {@code List<DocDetailDto.Step>} as JSON in a TEXT column.
 */
@Converter
public class DocStepListJsonConverter implements AttributeConverter<List<DocDetailDto.Step>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<DocDetailDto.Step>> STEP_LIST_TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<DocDetailDto.Step> attribute) {
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
    public List<DocDetailDto.Step> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<DocDetailDto.Step> parsed = MAPPER.readValue(dbData, STEP_LIST_TYPE);
            return parsed != null ? parsed : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
