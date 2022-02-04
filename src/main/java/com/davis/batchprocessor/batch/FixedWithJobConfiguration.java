package com.davis.batchprocessor.batch;


import com.davis.batchprocessor.classifier.CustomerClassifier;
import com.davis.batchprocessor.domain.CustomerRegister;
import com.davis.batchprocessor.domain.FieldSetA;
import com.davis.batchprocessor.listener.StepExecuteListener;
import com.davis.batchprocessor.mapper.MyLineMapper;
import com.davis.batchprocessor.matcher.FixedLengthMatchingCompositeLineTokenizer;
import com.davis.batchprocessor.matcher.RangeKey;
import com.davis.batchprocessor.repository.ClientRepository;
import com.davis.batchprocessor.repository.Customer2Repository;
import com.davis.batchprocessor.validator.FieldSetProcessValidator;
import com.davis.batchprocessor.validator.FieldSetValidator;
import com.davis.batchprocessor.writer.ClientItemWriter;
import com.davis.batchprocessor.writer.CustomItemWriter;
import com.davis.batchprocessor.writer.Customer2ItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class FixedWithJobConfiguration {


	@Autowired
	private DataSource dataSource;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private Customer2Repository customer2Repository;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	@Qualifier("jobLauncher1")
	private JobLauncher jobLauncher2;

	Logger logger = LoggerFactory.getLogger(FixedWithJobConfiguration.class);

	//@Bean
//	@StepScope


	public SynchronizedItemStreamReader<FieldSetA> itemReaderSync(Map<String, JobParameter>  parameters) {
		FlatFileItemReader<FieldSetA> itemReader = customerRegisterItemReader(parameters);

		SynchronizedItemStreamReader<FieldSetA> synchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
		synchronizedItemStreamReader.setDelegate(itemReader);
		return synchronizedItemStreamReader;
	}

	public FlatFileItemReader<FieldSetA> customerRegisterItemReader(
			Map<String, JobParameter>  parameters ) {

		return new FlatFileItemReaderBuilder<FieldSetA>()
				.name("customerRegisterItemReader")
				.resource(new FileSystemResource((String)parameters.get("customerFile").getValue()))
				//.fixedLength()
				/*.columns(new Range[]{new Range(1,11), new Range(12, 12), new Range(13, 22),
						new Range(23, 26), new Range(27,46), new Range(47,62), new Range(63,64),
						new Range(65,69)})
				.names(new String[] {"firstName", "middleInitial"})*/
			//	.lineTokenizer(fixedLengthTokenizer())
			//	.fieldSetMapper(customerUpdateFieldSetMapper())
				.lineMapper(MyLineMapper.builder()
						.tokenizer(fixedLengthTokenizer())
						.fieldSetMapper(customerUpdateFieldSetMapper())
						.build())
			//	.lineMapper(new PassThroughLineMapper())
			//	.targetType(Customer2.class)
			//	.distanceLimit(1)
				.build();
	}

	@Bean
	@StepScope
	public LineTokenizer fixedLengthTokenizer() {
		FixedLengthMatchingCompositeLineTokenizer compositeLineTokenizer = new FixedLengthMatchingCompositeLineTokenizer();
		FixedLengthTokenizer tokenizer1 = new FixedLengthTokenizer();

		tokenizer1.setNames(new String[] {"first_name", "tipo_reg", "middle_initial","filler"});
		tokenizer1.setColumns(new Range[]{new Range(1,10),new Range(11,12), new Range(12, 15), new Range(15, 25)});

		FixedLengthTokenizer tokenizer2 = new FixedLengthTokenizer();

		tokenizer2.setNames(new String[] {"first_name",  "tipo_reg","middle_initial","last_name"});
		tokenizer2.setColumns(new Range[]{new Range(1,10), new Range(11,12), new Range(12, 15), new Range(15, 25)});


		Map<RangeKey, LineTokenizer> tokenizers = new HashMap<>(3);
		tokenizers.put(new RangeKey(new Range(11,12),"AB"), tokenizer1);
		tokenizers.put(new RangeKey(new Range(11,12),"CD"), tokenizer2);
		compositeLineTokenizer.setTokenizers(tokenizers);

		return compositeLineTokenizer;
	}

	@Bean
	@StepScope
	public FieldSetMapper<FieldSetA> customerUpdateFieldSetMapper() {
	/*	return fieldSet -> {
			Map<String,String> map = new HashMap<>();
			for(String name : fieldSet.getNames())
				map.put(name, fieldSet.readString(name));
			return map; };*/
		return fieldSet -> {
			return new FieldSetA(0, fieldSet);
		};
	/*	return fieldSet -> {
			switch (fieldSet.readString("tipo_reg")) {
				case "AB": return new FieldSet(fieldSet); return new Customer2(
						fieldSet.readString("firstName"),
						fieldSet.readString("middleInitial"),
						fieldSet.readString("tipoReg"))

				case "CD": return new FieldSet(fieldSet);return new Client(
						fieldSet.readString("firstName"),
						fieldSet.readString("middleInitial"),
						fieldSet.readString("lastName"),
						fieldSet.readString("tipoReg"))

				default: throw new IllegalArgumentException("Invalid record type was found:" + fieldSet.readString("tipo_reg"));
			}
		};*/
	}

	@Bean
	@StepScope
	public ItemWriter<CustomerRegister> customerRegisterItemWriter() throws InterruptedException {

		return (items) -> items.forEach(System.out::println);
		//return i -> i.size();
	}

	@Bean
	@StepScope
	public Step downloadFileStep(Map<String, JobParameter> parameters) throws Exception {
		Tasklet tasklet = (contribution, context) -> {
			logger.info("Downloading File ->"
					+ context.getStepContext().getJobParameters().get("customerFile"));
			return RepeatStatus.FINISHED;
		};
		return  this.stepBuilderFactory.get("downloadFileStep").tasklet(tasklet).build();
	}
	@Bean
	@StepScope
	public Step customerRegisterFileStep(Map<String, JobParameter> parameters) throws Exception {


		TaskletStep copyFileStep = this.stepBuilderFactory.get("customerRegisterFileStep")//.tasklet(tasklet).build();

				.<FieldSetA, FieldSetA>chunk(10000)
				.reader(itemReaderSync(parameters))
				//.processor(validatingItemProcessor())
				.writer(classifierCompositeItemWriter(parameters))
				//.writer(new Customer2ItemWriter(dataSource))
				//.faultTolerant()
				//.skipLimit(500000)
				//.skip(Exception.class)
				//.listener(new MySkipListener(new File("error.txt"))
				//.taskExecutor(new SimpleAsyncTaskExecutor())
				.taskExecutor(taskExecutor())
				.listener(new JobParameterExecutionContextCopyListener())
				.listener(new StepExecuteListener())

				.build();
		return copyFileStep;
	}

	@Bean
	public ItemProcessor<FieldSetA, FieldSetA> itemProcessorValidator() {
		return new FieldSetProcessValidator();
	}

	//Validator
	@Bean
	public ValidatingItemProcessor<FieldSetA> validatingItemProcessor() {
		ValidatingItemProcessor<FieldSetA> itemProcessor = new ValidatingItemProcessor<>(new FieldSetValidator());

		itemProcessor.setFilter(true);//true=> skip
										//false = throw exception

		return itemProcessor;
	}

	// Classifier
	@Bean
	@StepScope
	public ClassifierCompositeItemWriter<FieldSetA> classifierCompositeItemWriter(Map<String, JobParameter> jobParameterMap) throws Exception {
		Classifier<FieldSetA, ItemWriter<? super FieldSetA>> classifier = new CustomerClassifier(dataSource, customer2ItemWriter(jobParameterMap), customer3ItemWriter(jobParameterMap));
		return new ClassifierCompositeItemWriterBuilder<FieldSetA>()
				.classifier(classifier)
				.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(2);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setThreadNamePrefix("MultiThreaded-");
		executor.setWaitForTasksToCompleteOnShutdown(false);
		executor.initialize();
		return executor;
	}

/*	@Bean
	public Job job(@Value("#{jobParameters['customerFile']}") String fileName) throws Exception {
		return this.jobBuilderFactory.get("job")
				.start(customerRegisterFileStep(fileName))
				.build();
	}*/

	@Bean
	@StepScope
	public CustomItemWriter customItemWriter() {
		return new CustomItemWriter(dataSource);
	}

	@Bean
	@StepScope
	public ClientItemWriter clientItemWriter() {
		return new ClientItemWriter(dataSource, clientRepository);
	}

	@Bean
	@StepScope
	public Customer2ItemWriter customer2ItemWriter(Map<String, JobParameter> jobParameterMap) {
		return new Customer2ItemWriter(dataSource, jobParameterMap);
	}

	@Bean
	@StepScope
	public Customer2ItemWriter customer3ItemWriter(Map<String, JobParameter> jobParameterMap) {
		return new Customer2ItemWriter(dataSource, jobParameterMap);
	}



}

