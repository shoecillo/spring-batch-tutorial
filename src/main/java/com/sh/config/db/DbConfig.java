package com.sh.config.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * 
 * @author shoe011
 * DataBases configuration class
 *
 */
@Configuration
public class DbConfig 
{
	/**
	 * Datasource for write process
	 * @return Datasource configured datasource
	 */
	@Bean(name="dsDestino")
    @Qualifier("dsDestino")
    @Primary
	public DataSource dataSourceDestino() 
	{	
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.HSQL) //.H2 or .DERBY
			.addScript("schema-all.sql")
			.build();
		return db;
	}
    /**
     * Datasource for read and extract results
     * @return Datasource configured datasource
     */
    @Bean(name="dsOrigen")
    @Qualifier("dsOrigen")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/batchjobs");
        dataSource.setUsername("root");
        dataSource.setPassword("zhent011");
         
        return dataSource;
    }
}
