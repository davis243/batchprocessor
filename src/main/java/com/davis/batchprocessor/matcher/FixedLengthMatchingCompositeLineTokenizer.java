package com.davis.batchprocessor.matcher;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;

public class FixedLengthMatchingCompositeLineTokenizer implements LineTokenizer, InitializingBean {

    private FixedLengthMatcher<LineTokenizer> tokenizers = null;
    public FieldSet tokenize(String line) {
        return ((LineTokenizer)this.tokenizers.match(line)).tokenize(line);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(this.tokenizers != null, "The 'tokenizers' property must be non-empty");
    }

    public void setTokenizers(Map<RangeKey, LineTokenizer> tokenizers) {
        Assert.isTrue(!tokenizers.isEmpty(), "The 'tokenizers' property must be non-empty");
        this.tokenizers = new FixedLengthMatcher(tokenizers);
    }
}
