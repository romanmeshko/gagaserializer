package data;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

class InnerTestClass {

    private LinkedList<String> strings;

    InnerTestClass() {
        this.strings = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            strings.add(UUID.randomUUID().toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InnerTestClass)) return false;
        InnerTestClass that = (InnerTestClass) o;
        return Objects.equals(strings, that.strings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strings);
    }
}

