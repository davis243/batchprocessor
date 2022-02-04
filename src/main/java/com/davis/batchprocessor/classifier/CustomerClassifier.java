package com.davis.batchprocessor.classifier;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.CustomerRegister;
import com.davis.batchprocessor.domain.FieldSetA;
import com.davis.batchprocessor.domain.FieldSetMain;
import com.davis.batchprocessor.writer.ClientItemWriter;
import com.davis.batchprocessor.writer.Customer2ItemWriter;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.classify.Classifier;

import javax.sql.DataSource;
import java.util.Map;

@StepScope
public class CustomerClassifier implements Classifier<FieldSetA, ItemWriter<? super FieldSetA>> {
private static final long serialVersionUID = 1L;

    private DataSource dataSource;

    Customer2ItemWriter customer2ItemWriter;

    Customer2ItemWriter customer3ItemWriter;


    public CustomerClassifier(DataSource dataSource, Customer2ItemWriter customer2ItemWriter,
                              Customer2ItemWriter customer3ItemWriter) {
        this.dataSource = dataSource;
        this.customer2ItemWriter = customer2ItemWriter;
        this.customer3ItemWriter = customer3ItemWriter;

}


    @Override
    public ItemWriter<FieldSetA> classify(FieldSetA fieldSet) {
      //  if (customer instanceof Client) {
        if(fieldSet.getFieldSet().readString("tipo_reg").equals("AB"))
            return customer2ItemWriter;
        else
            return customer3ItemWriter;
      //  }/* else {
        //    return customer2ItemWriter;
     //   }*/
    }


}
