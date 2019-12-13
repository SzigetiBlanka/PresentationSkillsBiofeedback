package com.aut.presentationskills.ui.measurement;

import com.aut.presentationskills.model.Measurement;
import com.aut.presentationskills.model.User;

public interface MeasurementScreen {
    void showMeasurementDatas(Measurement measurement);
    void showNetworkError(String errorMsg);

    void getMeasurementDataswithUserId(User user);

    void loadAgainMeasurementDatay();
}
