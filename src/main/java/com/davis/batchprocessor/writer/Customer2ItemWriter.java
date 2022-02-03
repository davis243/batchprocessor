package com.davis.batchprocessor.writer;

import com.davis.batchprocessor.constants.DataType;
import com.davis.batchprocessor.domain.FieldSetMain;
import com.davis.batchprocessor.mapper.FileMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Customer2ItemWriter implements ItemWriter<FieldSetMain> {
//your sql statement here
private String SQL = "INSERT INTO [table] ([columns]) VALUES ([params]);";

    private DataSource dataSource;

    private String tableName;


    public Customer2ItemWriter(DataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    static AtomicInteger atom = new AtomicInteger();

    @Override
    public void write(List<? extends FieldSetMain> list) throws Exception {
        System.out.println("Customer2:" + list.size());
        Connection connection = dataSource.getConnection();
        System.out.println("got Connection");
      //  connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(getSQL(list.get(0)));
        try {
            int count = 0;
            for (FieldSetMain row : (List<FieldSetMain>)list) {
                count++;
            /*    preparedStatement.setString(1, row.readString("firstName"));
                preparedStatement.setString(2, row.readString("middleInitial"));
                preparedStatement.setString(3, row.readString("tipoReg"));*/
                // preparedStatement.setDate(3, new java.sql.Date(new Date().getTime()));
                // Add it to the batch
             //   buildPrepareStatement(preparedStatement, row);
              //  preparedStatement.addBatch();

            }
            atom.getAndAdd(count);

         //   int[] count2 = preparedStatement.executeBatch();
        }
        finally {
            preparedStatement.close();
            connection.close();
        }
        System.out.println("atom:"+atom);

    }
    private void buildPrepareStatement(PreparedStatement preparedStatement, FieldSetMain row) throws SQLException {
        List<FileMapper> listColumnsMapped = getFileMapper().stream()
                .filter(a -> a.getTableName().equals(this.tableName))
                .collect(Collectors.toList());
        int index = 0;
        for(FileMapper column : listColumnsMapped){
            index++;
            if(DataType.ALPHANUMERIC.label.equals(column.getDataType())){
                preparedStatement.setString(index, row.getFieldSet().readString(column.getFieldName()));
            }
        }
    }
    private Map<String,String> getListColumnsAndParamValues(FieldSetMain fieldSet){
        StringBuilder sbColums = new StringBuilder();
        StringBuilder sbParams = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        for(String name : fieldSet.getFieldSet().getNames()){
            sbColums.append(name);
            sbColums.append(",");
            sbParams.append("?");
            sbParams.append(",");
        }
        sbColums.deleteCharAt(sbColums.length()-1);
        sbParams.deleteCharAt(sbParams.length()-1);
        map.put("columns", sbColums.toString());
        map.put("params", sbParams.toString());
        return map;
    }


    private String getSQL(FieldSetMain fieldSet){
        Map<String,String> mapColumnsParams = getListColumnsAndParamValues(fieldSet);
        SQL= SQL.replace("[table]", tableName);
        SQL= SQL.replace("[columns]", mapColumnsParams.get("columns"));
        SQL= SQL.replace("[params]", mapColumnsParams.get("params"));
        return SQL;
    }

    public List<FileMapper> getFileMapper(){
        List<FileMapper> list = new ArrayList<>();
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("first_name")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(1)
                .finalPosition(10)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("tipo_reg")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(11)
                .finalPosition(12)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("middle_initial")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(12)
                .finalPosition(15)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("filler")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(15)
                .finalPosition(25)
                .build());
        //////
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("first_name")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(1)
                .finalPosition(10)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("tipo_reg")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(11)
                .finalPosition(12)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("middle_initial")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(12)
                .finalPosition(15)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("last_name")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(15)
                .finalPosition(25)
                .build());
        return list;
    }

}
