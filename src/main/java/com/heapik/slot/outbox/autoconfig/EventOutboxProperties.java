package com.heapik.slot.outbox.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "slot.event.outbox")
@Validated
public class EventOutboxProperties {
    /**
     * Enable / disable outbox publishing scheduler
     */
    private boolean schedulingEnabled = true;

    /**
     * Cron expression for outbox polling
     */
    private String schedulingCronExpression = "0 */15 * * * *";

    /**
     * Batch size for outbox polling
     */
    private int batchSize = 100;

    public boolean isSchedulingEnabled() {
        return schedulingEnabled;
    }

    public void setSchedulingEnabled(boolean schedulingEnabled) {
        this.schedulingEnabled = schedulingEnabled;
    }

    public String getSchedulingCronExpression() {
        return schedulingCronExpression;
    }

    public void setSchedulingCronExpression(String schedulingCronExpression) {
        this.schedulingCronExpression = schedulingCronExpression;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
