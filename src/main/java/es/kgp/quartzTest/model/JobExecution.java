package es.kgp.quartzTest.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by carlos.garcia on 01/04/2014.
 */
@Entity
public class JobExecution implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date executionTime;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date previousExecutionTime;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextExecutionTime;

    @Column
    private String instanceExecutionId;

    @Column
    private String jobExecutionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Date getPreviousExecutionTime() {
        return previousExecutionTime;
    }

    public void setPreviousExecutionTime(Date previousExecutionTime) {
        this.previousExecutionTime = previousExecutionTime;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public String getInstanceExecutionId() {
        return instanceExecutionId;
    }

    public void setInstanceExecutionId(String instanceExecutionId) {
        this.instanceExecutionId = instanceExecutionId;
    }

    public String getJobExecutionName() {
        return jobExecutionName;
    }

    public void setJobExecutionName(String jobExecutionName) {
        this.jobExecutionName = jobExecutionName;
    }
}
