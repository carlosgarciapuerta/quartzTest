package es.kgp.quartzTest.config;

import es.kgp.quartzTest.job.DatabaseCleanJob;
import es.kgp.quartzTest.service.DatabaseCleanService;
import org.apache.commons.dbcp.BasicDataSource;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Check this urls:
 *
 * http://xkcb.blogspot.co.uk/2013/08/how-to-use-quartz-22-with-spring-32x-on.html
 * http://myshittycode.com/2013/09/26/configuring-quartz-scheduler-to-run-in-clustered-environment/
 *
 */
@Configuration
@ComponentScan(basePackages = {"es.kgp.quartzTest"})
@PropertySource("classpath:/application.properties")
@EnableJpaRepositories(value = "es.kgp.quartzTest.repository")
@EnableTransactionManagement
public class InDatabaseApplicationConfig {

    private static final String QUARTZ_CRON_EVERY_10_SECONDS = "0/10 * * * * ?";
    private static final String QUARTZ_CRON_EVERY_5_MINUTES = "0 0/5 * * * ?";
    private static final String QUARTZ_CRON_EVERY_DAY_AT_6_AM = "0 0 6 1/1 * ? *";

    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.databaseDriver}")
    private String databaseDriver;
    @Value("${database.url}")
    private String url;
    @Value("${database.databaseType}")
    private String databaseType;

    @Autowired
    private DatabaseCleanService databaseCleanService;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(databaseDriver);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(){
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("es.kgp.quartzTest.model");
        //factoryBean.setMappingResources("META-INF/orm.xml");
        factoryBean.afterPropertiesSet();
        HashMap<String, Object> jpaProperties = new HashMap<String, Object>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        factoryBean.setJpaPropertyMap(jpaProperties);

        return factoryBean.getObject();
    }

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        quartzScheduler.setQuartzProperties(quartzProperties());
        quartzScheduler.setDataSource(dataSource());
        quartzScheduler.setTransactionManager(transactionManager());
        quartzScheduler.setOverwriteExistingJobs(true);
        quartzScheduler.setSchedulerName("quartz-scheduler");

        //quartzScheduler.setTaskExecutor(quartzTaskExecutor());

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);

        /*JobDetail[] jobDetails = {defaultJobDetails().getObject()};
        quartzScheduler.setJobDetails(jobDetails);*/

        Trigger[] triggers = { processMyJobTrigger().getObject() };
        quartzScheduler.setTriggers(triggers);

        return quartzScheduler;
    }

    @Bean
    public CronTriggerFactoryBean processMyJobTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processMyJob().getObject());
        cronTriggerFactoryBean.setCronExpression(QUARTZ_CRON_EVERY_10_SECONDS);
        cronTriggerFactoryBean.setGroup("spring-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean processMyJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(DatabaseCleanJob.class);
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }



}
