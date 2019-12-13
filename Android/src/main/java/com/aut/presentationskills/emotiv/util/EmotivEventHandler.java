package com.aut.presentationskills.emotiv.util;

import android.util.Log;

import com.aut.presentationskills.emotiv.Constants;
import com.unity3d.player.UnityPlayer;

public class EmotivEventHandler implements SensorListener {
    @Override
    public void sensorStatusChanged(String name, Constants.Status status) {
        Log.w("sensor", "status changed:" + name + status);
        UnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setSensorStatus", status.toString());
    }
    @Override
    public void videoUrlChanged(String videoUrl) {
        Log.w("sensor", "video url changed:" + videoUrl);
        UnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setNewVideoUrl", videoUrl);
    }

    @Override
    public void performanceMetricChanged() {

    }



}
