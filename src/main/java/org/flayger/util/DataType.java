package org.flayger.util;

public enum DataType {
    INTEGER,
    FLOAT,
    STRING;

    public String fileName() {
        return switch (this) {
            case INTEGER -> "integers.txt";
            case FLOAT -> "floats.txt";
            case STRING -> "strings.txt";
        };
    }
}
