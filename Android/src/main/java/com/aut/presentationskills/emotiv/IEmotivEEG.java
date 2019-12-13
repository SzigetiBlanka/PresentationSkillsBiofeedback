package com.aut.presentationskills.emotiv;

import java.util.HashMap;
import java.util.Map;

import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_ENGAGEMENT;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_EXCITEMENT;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_FOCUS;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_INTEREST;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_RELAXATION;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_STRESS;

public interface IEmotivEEG {

    void sendStatus();

    static Map<Integer, String> createPmNames() {
        Map<Integer, String> pmNames = new HashMap<>();
        pmNames.put(PM_EXCITEMENT, "excitement");
        pmNames.put(PM_RELAXATION, "relaxation");
        pmNames.put(PM_STRESS, "stress");
        pmNames.put(PM_ENGAGEMENT, "engagement");
        pmNames.put(PM_INTEREST, "interest");
        pmNames.put(PM_FOCUS, "focus");
        return pmNames;
    }

    void connect();

    void disconnect();
}
