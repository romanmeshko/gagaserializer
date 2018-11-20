package io.shooroop.gaga.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Settings {
    String BEGIN_OBJECT = "{";
    String END_OBJECT = "}";
    String DELIMITER = ";";
    String TYPE_NAME_DELIMIT = "|" ;
    String NAME_VALUE_DELIMIT = ":";
    String COLLECT_BEGIN = "[";
    String COLLECT_END = "]";
    String COLLECT_DELIMIT = ",";
    String MAP_DELIMIT = "=";

    Charset CHARSET = StandardCharsets.UTF_8;

    interface Tags {
        String PRIMITIVE = "p";
        String ARRAY = "a";
        String OBJECT = "o";
        String COLLECTION = "c";
        String MAP = "m";
    }

}
