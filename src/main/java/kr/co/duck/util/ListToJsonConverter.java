package kr.co.duck.util;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListToJsonConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        // 리스트를 콤마 구분된 문자열로 변환하면서 각 항목의 공백 제거
        return attribute != null 
                ? attribute.stream()
                           .map(String::trim)           // 각 항목의 공백 제거
                           .collect(Collectors.joining(",")) 
                : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        // 콤마 구분된 문자열을 리스트로 변환하면서 각 항목의 공백 제거
        return dbData != null && !dbData.isEmpty()
                ? Arrays.stream(dbData.split(","))
                        .map(String::trim)              // 각 항목의 공백 제거
                        .filter(s -> !s.isEmpty())      // 빈 항목 제거
                        .collect(Collectors.toList())
                : List.of();
    }
}
