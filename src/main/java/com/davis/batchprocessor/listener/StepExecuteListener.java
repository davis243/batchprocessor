package com.davis.batchprocessor.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class StepExecuteListener  extends StepExecutionListenerSupport {
    private static final Logger LOG = LoggerFactory.getLogger(StepExecuteListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // No action needed
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        LOG.info("StepExecutionListener - afterStep:getCommitCount= {}", stepExecution.getCommitCount());
        LOG.info("StepExecutionListener - afterStep:getFilterCount= {}", stepExecution.getFilterCount());
        LOG.info("StepExecutionListener - afterStep:getProcessSkipCount= {}", stepExecution.getProcessSkipCount());
        LOG.info("StepExecutionListener - afterStep:getReadCount= {}", stepExecution.getReadCount());
        LOG.info("StepExecutionListener - afterStep:getReadSkipCount= {}", stepExecution.getReadSkipCount());
        LOG.info("StepExecutionListener - afterStep:getRollbackCount= {}", stepExecution.getRollbackCount());
        LOG.info("StepExecutionListener - afterStep:getWriteCount= {}", stepExecution.getWriteCount());
        LOG.info("StepExecutionListener - afterStep:getWriteSkipCount= {}", stepExecution.getWriteSkipCount());
        LOG.info("StepExecutionListener - afterStep:getStepName= {}", stepExecution.getStepName());
        LOG.info("StepExecutionListener - afterStep:getSummary= {}", stepExecution.getSummary());
        LOG.info("StepExecutionListener - afterStep:getStartTime= {}", stepExecution.getStartTime());
        LOG.info("StepExecutionListener - afterStep:getStartTime= {}", stepExecution.getEndTime());
        LOG.info("StepExecutionListener - afterStep:getLastUpdated= {}", stepExecution.getLastUpdated());
        LOG.info("StepExecutionListener - afterStep:getExitStatus= {}", stepExecution.getExitStatus());
        LOG.info("StepExecutionListener - afterStep:getFailureExceptions= {}", stepExecution.getFailureExceptions());


        return null;
    }
}