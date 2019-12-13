package com.aut.presentationskills.emotiv;

public class Constants {

    public enum Status {
        OFFLINE, UNRESPONSIVE, LOW_BATTERY, BAD_SIGNAL, CONNECTING, ONLINE
    }
    public static class Sensors {
        public static class EmotivEEG {

            public static final String ACTION_RESPONSE_STATUS = "hu.bme.aut.adapted.sensor.emotiv.response.status";
        }
    }

}
