package com.davis.batchprocessor.validator;

import com.davis.batchprocessor.domain.FieldSetA;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileParseException;

public class FieldSetProcessValidator implements ItemProcessor<FieldSetA,FieldSetA> {
    @Override
    public FieldSetA process(FieldSetA fieldSet) throws Exception {
        if (fieldSet.getFieldSet().readString("tipo_reg").equals("AB")) {
            throw new FlatFileParseException(String.format("Error line %s is TYPE %s", fieldSet.getLineNumber(),
                    fieldSet.getFieldSet().readString("tipo_reg")),
                    fieldSet.getFieldSet().readString("tipo_reg")
                    , fieldSet.getLineNumber());
        }
        return fieldSet;
    }
}

