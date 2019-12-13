package com.aut.presentationskills.emotiv.events;

public class PerformanceMetricUpdatedGameEvent {
    private final String metrics;

    public PerformanceMetricUpdatedGameEvent(String metrics) {
        this.metrics = metrics;
    }

    public String getMetrics() {
        return metrics;
    }
}
