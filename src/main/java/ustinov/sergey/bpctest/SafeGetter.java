package ustinov.sergey.bpctest;

import java.util.function.Function;

import static java.util.Optional.ofNullable;

public class SafeGetter<T> {
    public static final String EMPTY = "";

    private T instance;

    public SafeGetter(T o) {
        instance = o;
    }

    public <R> R get(Function<T, R> function, R def) {
        return ofNullable(instance).map(function).orElse(def);
    }

    public String get(Function<T, String> function) {
        return get(function, EMPTY);
    }
}