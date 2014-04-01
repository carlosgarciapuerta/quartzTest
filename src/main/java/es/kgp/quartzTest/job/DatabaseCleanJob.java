package es.kgp.quartzTest.job;

import es.kgp.quartzTest.model.JobExecution;
import es.kgp.quartzTest.repository.JobExecutionRepository;
import es.kgp.quartzTest.service.DatabaseCleanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by carlos.garcia on 01/04/2014.
 */
@Component
public class DatabaseCleanJob implements Job{

    @Autowired
    private JobExecutionRepository jobExecutionRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobExecution jobExecution = new JobExecution();
        jobExecution.setInstanceExecutionId(jobExecutionContext.getFireInstanceId());
        jobExecution.setJobExecutionName(jobExecutionContext.getTrigger().getJobKey().getName());
        jobExecution.setExecutionTime(jobExecutionContext.getFireTime());
        jobExecution.setPreviousExecutionTime(jobExecutionContext.getPreviousFireTime());
        jobExecution.setNextExecutionTime(jobExecutionContext.getNextFireTime());
        jobExecutionRepository.save(jobExecution);
    }
}
