package ustinov.sergey.bpctest.utils;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class SafeGetter<T> {
    public static final String EMPTY = "";

    private T instance;

    public SafeGetter(T o) {
        instance = o;
    }

    public <R> R get(@Nonnull Function<T, R> function, R def) {
        return get(function, o -> true, def);
    }

    public String get(@Nonnull Function<T, String> function) {
        return get(function, EMPTY);
    }

    public <R> R get(@Nonnull Function<T, R> function, @Nonnull Predicate<R> validation, R def) {
        R r = ofNullable(instance).map(function).orElse(def);
        if (!validation.test(r)) {
            throw new IllegalArgumentException(format("%s", r));
        }
        return r;
    }

    public static String wrap(@Nonnull String s) {
        return "%" + s + "%";
    }
}