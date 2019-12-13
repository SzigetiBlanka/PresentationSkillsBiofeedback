package com.aut.presentationskills.interactor.user.event;

import com.aut.presentationskills.model.Measurement;

import java.util.List;

public class GetMeasurementEvent {

    private List<Measurement> measurement;
    private Throwable throwable;

    public GetMeasurementEvent(){}

    public GetMeasurementEvent(List<Measurement> measurement, Throwable throwable){
        this.measurement= measurement;
        this.throwable=throwable;
    }


    public List<Measurement> getMeasurement() {
        return measurement;
    }

    public void setMeasurement(List<Measurement> measurement) {
        this.measurement = measurement;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
