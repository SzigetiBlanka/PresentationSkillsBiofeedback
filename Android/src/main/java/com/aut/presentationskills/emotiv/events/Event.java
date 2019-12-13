package com.aut.presentationskills.emotiv.events;

public abstract class Event {
    private Throwable throwable;

    public Event() {
    }

    public Event(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
