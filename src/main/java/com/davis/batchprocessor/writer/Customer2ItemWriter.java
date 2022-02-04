package com.davis.batchprocessor.writer;

import com.davis.batchprocessor.constants.DataType;
import com.davis.batchprocessor.domain.FieldSetA;
import com.davis.batchprocessor.domain.FieldSetMain;
import com.davis.batchprocessor.mapper.FileMapper;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Customer2ItemWriter implements ItemWriter<FieldSetA> {
//your sql statement here


    private DataSource dataSource;

    private Map<String, JobParameter> jobParameterMap;

    public Customer2ItemWriter(DataSource dataSource, Map<String, JobParameter> jobParameterMap) {
        this.dataSource = dataSource;
        this.jobParameterMap = jobParameterMap;

    }

    static AtomicInteger atom = new AtomicInteger();
    private StepExecution stepExecution;

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends FieldSetA> list) throws Exception {
        System.out.println("Customer2:" + list.size());
        Connection connection = dataSource.getConnection();
        System.out.println("got Connection");

      //  connection.setAutoCommit(false);
       String tableName = getTableName(list.get(0).getFieldSet().readString("tipo_reg"));
        System.out.println( "table Name: "+ tableName+ getSQL(list.get(0).getFieldSet(), tableName));
        PreparedStatement preparedStatement = connection.prepareStatement(getSQL(list.get(0).getFieldSet(), tableName));
        try {
            int count = 0;
            for (FieldSetA row : (List<FieldSetA>)list) {
             /*   if(row.readString("tipo_reg").equals("AB")){
                    throw new ValidationException("Error is TYPE AB");
                }*/
                count++;
            /*    preparedStatement.setString(1, row.readString("firstName"));
                preparedStatement.setString(2, row.readString("middleInitial"));
                preparedStatement.setString(3, row.readString("tipoReg"));*/
                // preparedStatement.setDate(3, new java.sql.Date(new Date().getTime()));
                // Add it to the batch
                buildPrepareStatement(preparedStatement, row.getFieldSet(), tableName);
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
    private void buildPrepareStatement(PreparedStatement preparedStatement, FieldSet row, String tableName) throws SQLException {

        String country = (String)jobParameterMap.get("country").getValue();
        String fileName = new File((String)jobParameterMap.get("customerFile").getValue()).getName();

        List<FileMapper> listColumnsMapped = getFileMapper().stream()
                .filter(a -> a.getTableName().equals(tableName) &&
                        a.getCountry().equals(country) &&
                                a.getInterfaceName().equals(fileName) &&
                                a.getRecordType().equals(row.readString("tipo_reg")))
                .collect(Collectors.toList());
        int index = 0;
        for(FileMapper column : listColumnsMapped){
            index++;
            if(DataType.ALPHANUMERIC.label.equals(column.getDataType())){
                preparedStatement.setString(index, row.readString(column.getFieldName()));
            }
        }
    }

    private String getTableName(String recordType){
       /* ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        String country = stepContext.getString("country");
        String fileName = new File(stepContext.getString("customerFile")).getName();*/
        String country = (String)jobParameterMap.get("country").getValue();
        String fileName = new File((String)jobParameterMap.get("customerFile").getValue()).getName();
        Optional<FileMapper> element = getFileMapper().stream()
                .filter(a -> a.getCountry().equals(country) &&
                        a.getInterfaceName().equals(fileName) &&
                        a.getRecordType().equals(recordType))
                .findAny();
        System.out.println("recortTYpe:: "+recordType+"table name: " + element.get().getTableName());
        return element.get().getTableName();
    }

    private Map<String,String> getListColumnsAndParamValues(FieldSet fieldSet){
        StringBuilder sbColums = new StringBuilder();
        StringBuilder sbParams = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        for(String name : fieldSet.getNames()){
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


    private String getSQL(FieldSet fieldSet, String tableName){
        String SQL = "INSERT INTO [table] ([columns]) VALUES ([params]);";
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
                .interfaceName("fixed.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("first_name")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(1)
                .finalPosition(10)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("tipo_reg")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(11)
                .finalPosition(12)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("middle_initial")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(12)
                .finalPosition(15)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed.txt")
                .recordType("CD")
                .tableName("customer2")
                .fieldName("last_name")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(15)
                .finalPosition(25)
                .build());
        //////FIXED 2
        /////
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("first_name")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(1)
                .finalPosition(10)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("tipo_reg")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(11)
                .finalPosition(12)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
                .recordType("AB")
                .tableName("client")
                .fieldName("middle_initial")
                .dataType(DataType.ALPHANUMERIC.label)
                .initialPosition(12)
                .finalPosition(15)
                .build());
        list.add(FileMapper.builder()
                .country("PE")
                .interfaceName("fixed2.txt")
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
