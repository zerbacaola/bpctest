package ustinov.sergey.bpctest;

import static java.lang.String.format;

public enum Gender {
    MALE("M"), FEMALE("F");

    private String value;

    Gender(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public static Gender parse(String v) {
        for (Gender g : Gender.values()) {
            if (g.value.equals(v)) {
                return g;
            }
        }
        throw new IllegalArgumentException(format("Unable to parse value: %s", v));
    }
}