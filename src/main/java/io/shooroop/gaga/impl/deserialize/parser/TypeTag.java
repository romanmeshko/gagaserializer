package io.shooroop.gaga.impl.deserialize.parser;

import io.shooroop.gaga.impl.Settings.*;

import java.util.stream.Stream;

import static io.shooroop.gaga.impl.Settings.*;

public enum TypeTag {
    PRIMITIVE(Tags.PRIMITIVE, DELIMITER),
    ARRAY(Tags.ARRAY, COLLECT_END + DELIMITER),
    OBJECT(Tags.OBJECT, END_OBJECT + DELIMITER),
    COLLECTION(Tags.COLLECTION, COLLECT_END + DELIMITER),
    MAP(Tags.MAP, COLLECT_END + DELIMITER),
    ;

    public String tag;
    public String endMark;

    TypeTag(String tag, String endMark) {
        this.tag = tag;
        this.endMark = endMark;
    }

    public static TypeTag of(String tg) {
        return Stream.of(values()).
                filter(e -> e.tag.equals(tg)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException("No Type Tag with name: " + tg));
    }
}
