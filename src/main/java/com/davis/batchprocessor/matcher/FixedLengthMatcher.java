package com.davis.batchprocessor.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FixedLengthMatcher<S> {

    private Map<RangeKey, S> map = new HashMap();
    private List<String> sorted = new ArrayList();

    public FixedLengthMatcher(Map<RangeKey, S> map) {
        this.map = map;
    }

    public static boolean match(RangeKey range, String str) {

        String subStr = str.substring(range.getRange().getMin()-1,range.getRange().getMax());
       return subStr.equals(range.getValue()) ;
    }
    public S match(String line) {
        S value = null;
        Iterator var3 = this.map.keySet().iterator();

        while(var3.hasNext()) {
            RangeKey key = (RangeKey)var3.next();
            if (match(key, line)) {
                value = this.map.get(key);
                break;
            }
        }
        if (value == null) {
            throw new IllegalStateException("Could not find a matching pattern for key=[" + line + "]");
        } else {
            return value;
        }
    }
}
