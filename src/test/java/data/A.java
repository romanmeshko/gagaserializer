package data;

import java.util.Objects;

public class A {
    private String message;

    public A(String message) {
        this.message = message;
    }

    public A() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof A)) return false;
        A a = (A) o;
        return Objects.equals(message, a.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
