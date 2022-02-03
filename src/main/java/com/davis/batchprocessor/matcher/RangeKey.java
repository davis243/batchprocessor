package com.davis.batchprocessor.matcher;

import org.springframework.batch.item.file.transform.Range;

public class RangeKey {
    private Range range;
    private String value;

    public RangeKey(Range range, String value) {
        this.range = range;
        this.value = value;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
