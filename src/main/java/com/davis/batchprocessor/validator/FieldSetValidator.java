package com.davis.batchprocessor.validator;

import com.davis.batchprocessor.domain.FieldSetA;
import com.davis.batchprocessor.listener.StepExecuteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.io.FileWriter;

public class FieldSetValidator  implements Validator<FieldSetA> {
    private static final Logger LOG = LoggerFactory.getLogger(FieldSetValidator.class);

    @Override
    public void validate(FieldSetA fieldSet) throws ValidationException {

        if(fieldSet.getFieldSet().readString("tipo_reg").equals("AB")){
            String message = String.format("Error line %s is TYPE %s", fieldSet.getLineNumber(),
                    fieldSet.getFieldSet().readString("tipo_reg"));
        //    LOG.info(message);
            throw new ValidationException(message);

           /* throw new FlatFileParseException(String.format("Error line %s is TYPE %s", fieldSet.getLineNumber(),
                    fieldSet.getFieldSet().readString("tipo_reg")),
                    fieldSet.getFieldSet().readString("tipo_reg")
                    , fieldSet.getLineNumber());*/
        }
    }

   /* @Override
    public FieldSet process(FieldSet fieldSet) throws Exception {
        if(fieldSet.readString("tipo_reg").equals("AB")){
            throw new Exception("Error is TYPE AB");
        }
        return fieldSet;
    }*/
}
