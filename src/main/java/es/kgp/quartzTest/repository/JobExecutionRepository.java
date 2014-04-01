package es.kgp.quartzTest.repository;

import es.kgp.quartzTest.model.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by carlos.garcia on 01/04/2014.
 */
public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
}
