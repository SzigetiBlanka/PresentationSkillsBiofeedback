package com.aut.presentationskills.emotiv.util;

import com.aut.presentationskills.emotiv.Constants;

public interface SensorListener {

    void sensorStatusChanged(String name, Constants.Status status);

    void performanceMetricChanged();

    void videoUrlChanged(String videoUrl);


}
