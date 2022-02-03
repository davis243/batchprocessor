package com.davis.batchprocessor;

import com.davis.batchprocessor.constants.DataType;
import com.davis.batchprocessor.mapper.FileMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BatchprocessorApplicationTests {

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

	@Test
	void contextLoads() {
	}

}
