package io.shooroop.gaga.impl.deserialize.parser;

import io.shooroop.gaga.impl.Settings;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.shooroop.gaga.impl.Settings.END_OBJECT;

public class ParserToFieldRepresentation implements Parser<String, FieldRepresentation> {

    @Override
    public Map<String, FieldRepresentation> parse(String strObject) {
        Map<String, FieldRepresentation> nameFieldMap = new LinkedHashMap<>();
        parseRecursively(strObject.substring(1), nameFieldMap);
        return nameFieldMap;
    }

    private Map<String, FieldRepresentation> parseRecursively(String strField,
                                                              Map<String, FieldRepresentation> nameFieldMap) {
        if (END_OBJECT.equals(strField)) {
            return nameFieldMap;
        } else {
            return parseRecursively(parseToAccum(strField, nameFieldMap), nameFieldMap);
        }
    }

    private String parseToAccum(String strField, Map<String, FieldRepresentation> nameFieldMap) {
        int indexOfTag = strField.indexOf(Settings.TYPE_NAME_DELIMIT);
        if (indexOfTag == -1) {
            throw new IllegalArgumentException("No Tag delimiter in Object string representation: " + strField);
        }
        String tag = strField.substring(0, indexOfTag);

        int indexOfType = strField.indexOf(Settings.TYPE_NAME_DELIMIT, indexOfTag + 1);

        if (indexOfType == -1) {
            throw new IllegalArgumentException("No Type delimiter in Object string representation: " + strField);
        }

        String typeName = strField.substring(indexOfTag + 1, indexOfType);


        int indexOfValue = strField.indexOf(Settings.NAME_VALUE_DELIMIT, indexOfType + 1);

        if (indexOfValue == -1) {
            throw new IllegalArgumentException("No Value delimiter in Object string representation: " + strField);
        }

        String name = strField.substring(indexOfType + 1, indexOfValue);

        int valueEnd = strField.indexOf(
                TypeTag.of(tag).endMark,
                indexOfType + 1
        );

        if (valueEnd == -1) {
            throw new IllegalArgumentException("No End delimiter in Object string representation: " + strField);
        }

        valueEnd = valueEnd + TypeTag.of(tag).endMark.length() - 1;

        String value = strField.substring(
                indexOfValue + 1,
                valueEnd
        );
        nameFieldMap.put(name, new FieldRepresentation(tag, typeName, name, value));
        return strField.substring(valueEnd + 1);
    }

}
