package info.besiera.eztvdroid.rest;

import info.besiera.eztvdroid.rest.controllers.CacheController;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence-mysql.properties" })
@ComponentScan({ "info.besiera.eztvdroid.rest.dao" })
@EnableScheduling
public class AppConfig {

	@Autowired
	private Environment env;

	//@Bean public GCMController gcmService() { return new GCMController(); }
	@Bean public CacheController cacheService() { return new CacheController(); }


	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory() {
		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(restDataSource());
		sessionFactory
				.setPackagesToScan(new String[] { "info.besiera.eztvdroid.rest.dao.domain" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);

		return txManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public DataSource restDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));

		return dataSource;
	}

	final Properties hibernateProperties() {
		return new Properties() {
			private static final long serialVersionUID = 1L;
			{
				// setProperty("hibernate.hbm2ddl.auto",
				// env.getProperty("hibernate.hbm2ddl.auto"));
				setProperty("hibernate.hbm2ddl.auto", "update");
				setProperty("hibernate.dialect",
						env.getProperty("hibernate.dialect"));
				setProperty("hibernate.globally_quoted_identifiers", "true");
				//setProperty("hibernate.show_sql", "true");
			}
		};
	}
}