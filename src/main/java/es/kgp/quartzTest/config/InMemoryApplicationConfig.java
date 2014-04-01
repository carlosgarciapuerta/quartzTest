package es.kgp.quartzTest.config;

import es.kgp.quartzTest.service.DatabaseCleanService;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: carlos.garcia
 * Date: 19/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan(basePackages = {"es.kgp.quartzTest"})
public class InMemoryApplicationConfig {

    @Autowired
    private DatabaseCleanService databaseCleanService;

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        //quartzScheduler.setDataSource(dataSource);
        //quartzScheduler.setTransactionManager(transactionManager);
        quartzScheduler.setOverwriteExistingJobs(true);
        quartzScheduler.setSchedulerName("jelies-quartz-scheduler");

        quartzScheduler.setQuartzProperties(quartzProperties());

        Trigger[] triggers = { processMQTrigger().getObject() };
        quartzScheduler.setTriggers(triggers);

        return quartzScheduler;
    }

    @Bean
    public CronTriggerFactoryBean processMQTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(procesoMQJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0/5 * * * * ?");
        cronTriggerFactoryBean.setGroup("spring-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean
    public MethodInvokingJobDetailFactoryBean procesoMQJob() {
        MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactory.setTargetObject(databaseCleanService);
        jobDetailFactory.setTargetMethod("cleanExpiredSessions");
        jobDetailFactory.setGroup("spring3-quartz");
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
