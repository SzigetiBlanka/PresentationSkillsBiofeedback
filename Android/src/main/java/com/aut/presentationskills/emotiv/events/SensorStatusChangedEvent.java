package com.aut.presentationskills.emotiv.events;

import com.aut.presentationskills.emotiv.Constants.Status;

public class SensorStatusChangedEvent extends Event {
    private String name;
    private Status status;

    public SensorStatusChangedEvent(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
