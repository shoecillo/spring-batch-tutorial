package com.sh.config.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:application.properties")
public class DbConfig 
{
	@Value("${db.jdbc.driver}")
	private String driver;
	
	@Value("${db.jdbc.url}")
	private String dbUrl;
	
	@Value("${db.jdbc.user}")
	private String user;
	
	@Value("${db.jdbc.pass}")
	private String pass;
	
	@Value("${db.mem.script}")
	private String scriptInMem;
	
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
			.addScript(scriptInMem)
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
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
         
        return dataSource;
    }
}
