package com.aut.presentationskills.emotiv.events;

public class FrameworkEvent {

    private final Object gameEvent;
    private final long timestamp;
    private final boolean dynamic;

    public FrameworkEvent(Object gameEvent, long timestamp, boolean dynamic) {
        this.gameEvent = gameEvent;
        this.timestamp = timestamp;
        this.dynamic = dynamic;
    }

    public Object getGameEvent() {
        return gameEvent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isDynamic() {
        return dynamic;
    }
}
