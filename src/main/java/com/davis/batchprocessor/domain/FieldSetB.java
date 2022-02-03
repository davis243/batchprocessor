package com.davis.batchprocessor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.file.transform.FieldSet;

@AllArgsConstructor
@Getter
@Setter
public class FieldSetB extends FieldSetMain {

    private FieldSet fieldSet;
}