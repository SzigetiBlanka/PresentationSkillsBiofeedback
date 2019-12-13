package com.aut.presentationskills.emotiv.events;

import com.aut.presentationskills.emotiv.Constants;

public class SensorEvent {
    private String action;
    private Constants.Status status;

    public SensorEvent(String action, Constants.Status status) {
        this.action = action;
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public Constants.Status getStatus() {
        return status;
    }
}
