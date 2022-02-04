package com.davis.batchprocessor.mapper;

import com.davis.batchprocessor.domain.FieldSetA;
import lombok.Builder;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;

@Builder
public class MyLineMapper implements LineMapper<FieldSetA>, InitializingBean {
    private LineTokenizer tokenizer;

    private FieldSetMapper fieldSetMapper;

    @Override
    public FieldSetA mapLine(String line, int lineNumber) throws Exception {
        try{
            FieldSetA r = (FieldSetA) fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
            // this is the modification
            r.setLineNumber(lineNumber);
            return r;
        }
        catch(Exception ex){
            throw new FlatFileParseException("Parsing error at line: " + lineNumber +
                    ", input=[" + line + "]", ex, line, lineNumber);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
