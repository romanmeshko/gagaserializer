package io.shooroop.gaga.impl.deserialize.parser;

import java.util.Map;

public interface Parser<T, R> {

    Map<T, R> parse(T value);

}
