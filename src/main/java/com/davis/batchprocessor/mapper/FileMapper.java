package com.davis.batchprocessor.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FileMapper {

    private String country;
    private String interfaceName;
    private String recordType;
    private String tableName;
    private String fieldName;
    private String dataType;
    private String decimalScale;
    private Integer initialPosition;
    private Integer finalPosition;

}
