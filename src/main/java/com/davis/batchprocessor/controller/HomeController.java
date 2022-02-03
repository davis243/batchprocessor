package com.davis.batchprocessor.controller;

import com.davis.batchprocessor.batch.FixedWithJobConfiguration;
import com.davis.batchprocessor.constants.DataType;
import com.davis.batchprocessor.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class HomeController {

   @Autowired
    @Qualifier("jobLauncher1")
    private JobLauncher jobLauncher2;

   /* @Autowired
    @Qualifier("job")
    private Job job1;*/

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

	@Autowired
	FixedWithJobConfiguration withFixedTypeWidthJob;


    Logger logger = LoggerFactory.getLogger(HomeController.class);

  @GetMapping
	public ResponseEntity home(){Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("customerFile", new JobParameter("/Users/user/David/sandbox/batchprocessor/src/main/resources/fixed.txt"));
		confMap.put("uuid", new JobParameter(UUID.randomUUID().toString()));

		Map<String, JobParameter> confMap2 = new HashMap<>();
		confMap2.put("customerFile", new JobParameter("/Users/user/David/sandbox/batchprocessor/src/main/resources/fixed2.txt"));
		confMap2.put("uuid", new JobParameter(UUID.randomUUID().toString()));

		  Map<String, JobParameter> confMap3 = new HashMap<>();
		  confMap3.put("customerFile", new JobParameter("/Users/user/David/sandbox/batchprocessor/src/main/resources/fixed3.txt"));
		  confMap3.put("uuid", new JobParameter(UUID.randomUUID().toString()));

		JobParameters jobParameters = new JobParameters(confMap);
		JobParameters jobParameters2 = new JobParameters(confMap2);
		JobParameters jobParameters3 = new JobParameters(confMap3);

		try {

			// jobLauncher2.run((Job) job1, jobParameters);
			jobLauncher2.run((Job) this.jobBuilderFactory.get(confMap.get("customerFile").toString())
					.start(withFixedTypeWidthJob.downloadFileStep(confMap))
					.next(withFixedTypeWidthJob.customerRegisterFileStep(confMap))
					.build(), jobParameters);

	/*		jobLauncher2.run((Job) this.jobBuilderFactory.get(confMap2.get("customerFile").toString())
					.start(withFixedTypeWidthJob.downloadFileStep(confMap2))
					.next(withFixedTypeWidthJob.customerRegisterFileStep(confMap2))
					.build(), jobParameters2);

			jobLauncher2.run((Job) this.jobBuilderFactory.get(confMap3.get("customerFile").toString())
					.start(withFixedTypeWidthJob.downloadFileStep(confMap3))
					.next(withFixedTypeWidthJob.customerRegisterFileStep(confMap3))
					.build(), jobParameters3);*/

		}catch (Exception ex){
			logger.error(ex.getMessage());
		}

		return ResponseEntity.ok("Hello");
	}



}
