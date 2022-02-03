package com.davis.batchprocessor.classifier;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.CustomerRegister;
import com.davis.batchprocessor.writer.ClientItemWriter;
import com.davis.batchprocessor.writer.Customer2ItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

public class CustomerClassifier implements Classifier<CustomerRegister, ItemWriter<? super CustomerRegister>> {
private static final long serialVersionUID = 1L;
private transient ClientItemWriter clientItemWriter;
private transient  Customer2ItemWriter customer2ItemWriter;


public CustomerClassifier(ClientItemWriter fileItemWriter, Customer2ItemWriter jdbcItemWriter) {
        this.clientItemWriter = fileItemWriter;
        this.customer2ItemWriter = jdbcItemWriter;
        }


    @Override
    public ItemWriter<CustomerRegister> classify(CustomerRegister customer) {
        if (customer instanceof Client) {
            return clientItemWriter;
        } else {
            return customer2ItemWriter;
        }
    }
}
