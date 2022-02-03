package com.davis.batchprocessor.constants;

public enum DataType {
    ALPHANUMERIC("Alfanumerico"),
    NUMERIC("Numerico"),
    DATE("Date");

    public final String label;

    DataType(String value) {
        this.label = value;
    }
}
