package com.aut.presentationskills.emotiv.events;

public class VideoUrlEvent extends Event {
    private String url;

    public VideoUrlEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
