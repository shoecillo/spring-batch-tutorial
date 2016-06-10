package com.sh.config.readers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sh.model.Person;

@Configuration
public class ReadersConfig 
{
	/**
	 * Injected datasource for read values
	 */
	@Resource(name="dsOrigen")
    private DataSource dsOrigen;
	
	/**
	 * Item reader, execute SQL query and iterate over resultset through RowMapper
	 * @return JdbcPagingItemReader Configured reader
	 */
	 @Bean
	    public JdbcPagingItemReader<Person> readerJdbc()
	    {
	    	JdbcPagingItemReader<Person> reader = new JdbcPagingItemReader<Person>();
	    	MySqlPagingQueryProvider query = new MySqlPagingQueryProvider();
	    	query.setSelectClause("SELECT first_name,last_name");
	    	query.setFromClause("FROM people");
	    	Map<String, Order> sortConfiguration = new HashMap<>();
	        sortConfiguration.put("first_name", Order.ASCENDING);
	        query.setSortKeys(sortConfiguration);
	    	reader.setDataSource(dsOrigen);
	    	reader.setQueryProvider(query);
	    	reader.setRowMapper(new BeanPropertyRowMapper<>(Person.class));
	    	return reader;
	    }
	    /**
	     * XML reader and parser.Uses <b>Jaxb2Marshaller</b> for map the values.<br>
	     * Its have to configure the model class to use it with JAXB
	     * @return StaxEventItemReader Configured reader
	     */
	     @Bean
	     public StaxEventItemReader<Person> readerXml()
	     {
	    	 StaxEventItemReader<Person> xmlFileReader = new StaxEventItemReader<Person>();
	    	 
	    	 xmlFileReader.setResource(new ClassPathResource("data/persons.xml"));
	         xmlFileReader.setFragmentRootElementName("person");
	  
	         Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
	         studentMarshaller.setClassesToBeBound(Person.class);
	         xmlFileReader.setUnmarshaller(studentMarshaller);
	    	 
	    	 return xmlFileReader;
	    	 
	     }
	     /**
	      * Plain text reader with a custom pattern.
	      * @return FlatFileItemReader Configured reader
	      */
	     @Bean
	     public FlatFileItemReader<Person> readerPattern() {
	         FlatFileItemReader<Person> readerPattern = new FlatFileItemReader<Person>();
	         readerPattern.setResource(new ClassPathResource("data/persons.txt"));
	         readerPattern.setLineMapper(new DefaultLineMapper<Person>() {{
	             setLineTokenizer(new DelimitedLineTokenizer() 
	             {{
	            	 setDelimiter("##");
	                 setNames(new String[] { "firstName", "lastName" });
	             }});
	             setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
	                 setTargetType(Person.class);
	             }});
	         }});
	         return readerPattern;
	     }
	     

	    /**
	     * CSV reader, default comma separated
	     * @return FlatFileItemReader Configured reader
	     */
	    @Bean
	    public FlatFileItemReader<Person> reader() {
	        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
	        reader.setResource(new ClassPathResource("data/sample-data.csv"));
	        reader.setLineMapper(new DefaultLineMapper<Person>() {{
	            setLineTokenizer(new DelimitedLineTokenizer() {{
	                setNames(new String[] { "firstName", "lastName" });
	            }});
	            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
	                setTargetType(Person.class);
	            }});
	        }});
	        return reader;
	    }
}
