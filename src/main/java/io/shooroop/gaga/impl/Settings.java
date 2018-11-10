package io.shooroop.gaga.impl;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

interface Settings {
    char BEGIN_OBJECT = '{';
    char END_OBJECT = '}';
    char DELIMITER = ';';
    char TYPE_NAME_DELIMIT = ' ';
    char NAME_VALUE_DELIMIT = ':';
    char COLLECT_BEGIN = '[';
    char COLLECT_END = ']';
    char COLLECT_DELIMIT = ',';
    char MAP_DELIMIT = '=';

    Charset CHARSET = StandardCharsets.UTF_8;

    interface Enum {
        String BEGIN = "enum| ";
        String END = "|";
    }
}
