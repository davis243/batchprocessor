package com.davis.batchprocessor.classifier;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.CustomerRegister;
import com.davis.batchprocessor.domain.FieldSetMain;
import com.davis.batchprocessor.writer.ClientItemWriter;
import com.davis.batchprocessor.writer.Customer2ItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.classify.Classifier;

import javax.sql.DataSource;

public class CustomerClassifier implements Classifier<FieldSetMain, ItemWriter<? super FieldSetMain>> {
private static final long serialVersionUID = 1L;

    private DataSource dataSource;

public CustomerClassifier(DataSource dataSource) {
        this.dataSource = dataSource;
}


    @Override
    public ItemWriter<FieldSetMain> classify(FieldSetMain fieldSet) {
      //  if (customer instanceof Client) {
        if(fieldSet.getFieldSet().readString("tipo_reg").equals("AB"))
            return new Customer2ItemWriter(dataSource, "client");
        else
            return new Customer2ItemWriter(dataSource, "customer2");
      //  }/* else {
        //    return customer2ItemWriter;
     //   }*/
    }
}
