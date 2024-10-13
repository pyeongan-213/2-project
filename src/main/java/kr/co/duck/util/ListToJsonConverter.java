package kr.co.duck.util;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListToJsonConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        // 리스트를 콤마 구분된 문자열로 변환
        return attribute != null ? String.join(",", attribute) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        // 콤마 구분된 문자열을 리스트로 변환
        return dbData != null && !dbData.isEmpty()
                ? Arrays.stream(dbData.split(",")).map(String::trim).collect(Collectors.toList())
                : List.of();
    }
}
