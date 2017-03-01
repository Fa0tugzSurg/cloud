package com.qy.insurance.cloud.core.zipkin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import zipkin.reporter.ReporterMetrics;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/28 10:41
 * @version: 1.0.0
 */
@Slf4j
public class ReporterMetricsAdapter implements ReporterMetrics {

    private final SpanMetricReporter spanMetricReporter;

    public ReporterMetricsAdapter(SpanMetricReporter spanMetricReporter) {
        this.spanMetricReporter = spanMetricReporter;
    }

    @Override
    public void incrementMessages() {

    }

    @Override
    public void incrementMessagesDropped(Throwable cause) {
        log.debug("Increment Messages is Dropped:", cause);
    }

    @Override
    public void incrementSpans(int quantity) {
        this.spanMetricReporter.incrementAcceptedSpans(quantity);
    }

    @Override
    public void incrementSpanBytes(int quantity) {

    }

    @Override
    public void incrementMessageBytes(int quantity) {

    }

    @Override
    public void incrementSpansDropped(int quantity) {
        this.spanMetricReporter.incrementDroppedSpans(quantity);
    }

    @Override
    public void updateQueuedSpans(int update) {

    }

    @Override
    public void updateQueuedBytes(int update) {

    }
}
