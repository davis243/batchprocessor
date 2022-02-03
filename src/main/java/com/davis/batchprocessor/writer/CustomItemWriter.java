package com.davis.batchprocessor.writer;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.Customer2;
import com.davis.batchprocessor.domain.CustomerRegister;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomItemWriter implements ItemWriter<CustomerRegister> {
//your sql statement here
private static final String SQL = "INSERT INTO new_customer (first_name, last_name, birthdate) VALUES (?,?,?);";

    private DataSource dataSource;

    public CustomItemWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    static AtomicInteger atom = new AtomicInteger();

    @Override
    public void write(List<? extends CustomerRegister> list) throws Exception {
        System.out.println("getting Connection");
        Connection connection = dataSource.getConnection();
        System.out.println("got Connection");
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        try {
            int count = 0;
            for (CustomerRegister identity : list) {
                count++;

                if (identity instanceof Client) {
                    preparedStatement.setString(1, ((Client) identity).getFirstName());
                    preparedStatement.setString(2, ((Client) identity).getLastName());
                } else {
                    preparedStatement.setString(1, ((Customer2) identity).getFirstName());
                    preparedStatement.setString(2, ((Customer2) identity).getMiddleInitial());
                }

                preparedStatement.setDate(3, new java.sql.Date(new Date().getTime()));
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
