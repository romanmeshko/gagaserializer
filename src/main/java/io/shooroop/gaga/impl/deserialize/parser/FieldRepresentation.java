package io.shooroop.gaga.impl.deserialize.parser;

import static io.shooroop.gaga.impl.Settings.NAME_VALUE_DELIMIT;
import static io.shooroop.gaga.impl.Settings.TYPE_NAME_DELIMIT;

public class FieldRepresentation {
    public final String tag;
    public final String type;
    public final String name;
    public final String value;

    public FieldRepresentation(String tag,
                               String type,
                               String name,
                               String value) {
        this.tag = tag;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public static String presentationOf(TypeTag tag,
                                        String type,
                                        String name) {
        return tag.tag + TYPE_NAME_DELIMIT +
                type + TYPE_NAME_DELIMIT +
                name + NAME_VALUE_DELIMIT;
    }

    @Override
    public String toString() {
        return "FieldRepresentation{" +
                "tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
