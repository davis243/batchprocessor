package com.davis.batchprocessor.writer;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.Customer2;
import com.davis.batchprocessor.domain.CustomerRegister;
import com.davis.batchprocessor.repository.Customer2Repository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer2ItemWriter implements ItemWriter<CustomerRegister> {
//your sql statement here
private static final String SQL = "INSERT INTO customer2 (first_name, middle_Initial, tipo_reg) VALUES (?,?,?);";

    private DataSource dataSource;

    @Autowired
    private Customer2Repository customer2Repository;

    public Customer2ItemWriter(DataSource dataSource, Customer2Repository customer2Repository) {
        this.dataSource = dataSource;
        this.customer2Repository = customer2Repository;
    }

    static AtomicInteger atom = new AtomicInteger();

    @Override
    public void write(List<? extends CustomerRegister> list) throws Exception {
        System.out.println("Customer2:" + list.size());
        Connection connection = dataSource.getConnection();
        System.out.println("got Connection");
      //  connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        try {
            int count = 0;
            for (Customer2 identity : (List<Customer2>)list) {
                count++;
                preparedStatement.setString(1, identity.getFirstName());
                preparedStatement.setString(2, identity.getMiddleInitial());
                preparedStatement.setString(3, identity.getTipoReg());
                // preparedStatement.setDate(3, new java.sql.Date(new Date().getTime()));
                // Add it to the batch
                preparedStatement.addBatch();

            }
            atom.getAndAdd(count);

            int[] count2 = preparedStatement.executeBatch();
        }
        finally {
            preparedStatement.close();
            connection.close();
        }
        System.out.println("atom:"+atom);

    }

}
