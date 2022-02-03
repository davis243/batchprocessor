package com.davis.batchprocessor.writer;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.Customer2;
import com.davis.batchprocessor.domain.CustomerRegister;
import com.davis.batchprocessor.repository.ClientRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientItemWriter implements ItemWriter<CustomerRegister> {
//your sql statement here
private static final String SQL = "INSERT INTO client (first_name, last_name, middle_Initial, tipo_reg) VALUES (?,?,?,?);";

    private DataSource dataSource;

    private ClientRepository clientRepository;

    public ClientItemWriter(DataSource dataSource,ClientRepository clientRepository) {
        this.dataSource = dataSource;
        this.clientRepository = clientRepository;
    }

    static AtomicInteger atom = new AtomicInteger();



    @Override
    public void write(List<? extends CustomerRegister> list) throws Exception {
        System.out.println("Client:" + list.size());
     /*   clientRepository.saveAll((List<Client>) list);*/
       Connection connection = dataSource.getConnection();
        System.out.println("got Connection");
      //  connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        try {
            int count = 0;
            for (Client identity : (List<Client>)list) {
                count++;
                    preparedStatement.setString(1, identity.getFirstName());
                    preparedStatement.setString(2, identity.getLastName()==null? "": identity.getLastName());
                    preparedStatement.setString(3, identity.getMiddleInitial());
                    preparedStatement.setString(4, identity.getTipoReg());
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
