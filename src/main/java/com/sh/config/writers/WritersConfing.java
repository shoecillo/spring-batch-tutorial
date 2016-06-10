package com.sh.config.writers;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sh.model.Person;

@Configuration
public class WritersConfing 
{
	/**
	 * Datasource for writers
	 */
	@Resource(name="dsDestino")
    private DataSource dsDestino;
	
	/**
	 * Writer in Database through datasource and SQL query
	 * @return JdbcBatchItemWriter
	 */ 
	 @Bean
	    public JdbcBatchItemWriter<Person> writer() {
	        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
	        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
	        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
	        writer.setDataSource(dsDestino);
	        
	        return writer;
	    }
}
