package com.sh.config.batch;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sh.config.readers.ReadersConfig;
import com.sh.config.writers.WritersConfing;
import com.sh.model.Person;
import com.sh.notification.JobCompletionNotificationListener;
import com.sh.processor.PersonItemProcessor;

/**
 * 
 * @author shoe011
 * Configuration class for batch jobs and default datasource
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    /**
     * This object contains all configured readers methods.<br>
     * For add more readers, must do it in <b>ReadersConfig</b> class.
     */
    @Autowired
    private ReadersConfig readers;
    /**
    * This object contains all configured writers methods.<br>
    * For add more readers, must do it in <b>WritersConfing</b> class.
    */
    @Autowired
    private WritersConfing writers;
    /**
     * Datasource to write job.<br>
     * Its configured in class <b>com.sh.config.db.DbConfig</b>
     */
    @Resource(name="dsDestino")
    private DataSource dsDestino;
    
    /**
     * Configure default datasource
     * @param dataSource
     * @return
     */
    @Bean
    BatchConfigurer configurer(DataSource dataSource){
      return new DefaultBatchConfigurer(dsDestino);
    }
    /**
     * Inject custom ItemProcessor for apply batch job
     * @return PersonItemProcessor Logic for job
     */
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

   
   
    /**
     * Listener for notify the results of batch process.<br>
     * Need writer datasource.
     * @return JobExecutionListener Execution listener
     */
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener(new JdbcTemplate(dsDestino));
    }

   /**
    * Job Execution Flow.Add notification listener and step(s) to execute in this job 
    * @return Job - configured job
    */
    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build();
    }
    /**
     * Job Step, here its read, process and write.<br>
     * Chunk 10 to 10, if is needed this value can be changed.
     * @return Step current configured step
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(readers.readerJdbc())
                .processor(processor())
                .writer(writers.writer())
                .build();
    }
   
}
