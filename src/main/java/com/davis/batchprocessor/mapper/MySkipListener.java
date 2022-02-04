package com.davis.batchprocessor.mapper;

import com.davis.batchprocessor.domain.FieldSetA;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.validator.ValidationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class  MySkipListener implements SkipListener<FieldSetA, FieldSetA> {

    private FileWriter fileWriter;

    public MySkipListener(File file) throws IOException {
        this.fileWriter = new FileWriter(file);
    }

    @Override
    public void onSkipInRead(Throwable throwable) {
        if (throwable instanceof ValidationException) {
            FlatFileParseException flatFileParseException = (FlatFileParseException) throwable;
            try {
                fileWriter.write(flatFileParseException.getLineNumber());
            } catch (IOException e) {
                System.err.println("Unable to write skipped line to error file");
            }
        }
    }

    @Override
    public void onSkipInWrite(FieldSetA item, Throwable t) {
        System.out.println("Item " + item + " was skipped due to: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(FieldSetA item, Throwable t) {
        System.out.println("Item " + item + " was skipped due to: " + t.getMessage());
    }

}