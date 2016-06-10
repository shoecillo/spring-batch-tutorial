package com.sh.config.writers;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sh.model.Person;

@Configuration
@PropertySource("classpath:application.properties")
public class WritersConfing 
{
	/**
	 * Datasource for writers
	 */
	@Resource(name="dsDestino")
    private DataSource dsDestino;
	
	@Value("${export.file.xml}")
	private String xmlFile;
	
	@Value("${export.file.csv}")
	private String csvFile;
	
	@Value("${export.file.txt}")
	private String txtFile;
	
	/**
	 * Writer in Database through datasource and SQL query
	 * @return JdbcBatchItemWriter
	 */ 
	 @Bean
	    public JdbcBatchItemWriter<Person> jdbcWriter() {
	        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
	        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
	        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
	        writer.setDataSource(dsDestino);
	        
	        return writer;
	    }
	 
	    /**
	     * Writer in Xml file
	     * @return ItemWriter
	     */
	    @Bean
	    public ItemWriter<Person> xmlItemWriter() {
	        StaxEventItemWriter<Person> xmlFileWriter = new StaxEventItemWriter<>();
	 
	        
	        xmlFileWriter.setResource(new FileSystemResource(xmlFile));
	 
	        xmlFileWriter.setRootTagName("persons");
	 
	        Jaxb2Marshaller personMarshaller = new Jaxb2Marshaller();
	        personMarshaller.setClassesToBeBound(Person.class);
	        xmlFileWriter.setMarshaller(personMarshaller);
	 
	        return xmlFileWriter;
	    }
	    /**
	     * Writer in csv file with comma-based separator
	     * @return ItemWriter
	     */
	    @Bean
	    public ItemWriter<Person> csvItemWriter()
	    {
	    	FlatFileItemWriter<Person> csvWriter = new FlatFileItemWriter<Person>();
	    	csvWriter.setResource(new FileSystemResource(csvFile));
	    	csvWriter.setShouldDeleteIfExists(true);
	    	
	    	DelimitedLineAggregator<Person> lineAgg = new DelimitedLineAggregator<Person>();
	    	lineAgg.setDelimiter(",");
	    	BeanWrapperFieldExtractor<Person> extractor = new BeanWrapperFieldExtractor<Person>();
	    	extractor.setNames(new String[] { "firstName", "lastName" });
	    	lineAgg.setFieldExtractor(extractor);
	    	csvWriter.setLineAggregator(lineAgg);
	    	return csvWriter;
	    }
	    
	    /**
	     * Writer in txt file with custom token
	     * @return ItemWriter
	     */
	    @Bean
	    public ItemWriter<Person> txtItemWriter()
	    {
	    	FlatFileItemWriter<Person> txtWriter = new FlatFileItemWriter<Person>();
	    	txtWriter.setResource(new FileSystemResource(txtFile));
	    	txtWriter.setShouldDeleteIfExists(true);
	    	
	    	DelimitedLineAggregator<Person> lineAgg = new DelimitedLineAggregator<Person>();
	    	lineAgg.setDelimiter("##");
	    	BeanWrapperFieldExtractor<Person> extractor = new BeanWrapperFieldExtractor<Person>();
	    	extractor.setNames(new String[] { "firstName", "lastName" });
	    	lineAgg.setFieldExtractor(extractor);
	    	txtWriter.setLineAggregator(lineAgg);
	    	return txtWriter;
	    }
	    
}
