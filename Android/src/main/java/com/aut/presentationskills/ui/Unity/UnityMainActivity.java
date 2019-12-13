package com.aut.presentationskills.ui.Unity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.aut.presentationskills.PresentationSkillsVRandAndroid;
import com.aut.presentationskills.communication.MqttHelper;
import com.aut.presentationskills.emotiv.Constants;
import com.aut.presentationskills.emotiv.EmotivEEG;
import com.aut.presentationskills.emotiv.events.PerformanceMetricUpdatedGameEvent;
import com.aut.presentationskills.emotiv.events.SensorStatusChangedEvent;
import com.aut.presentationskills.emotiv.events.VideoUrlEvent;
import com.aut.presentationskills.emotiv.util.EmotivEventHandler;
import com.aut.presentationskills.emotiv.util.SensorListener;
import com.unity3d.player.UnityPlayer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class UnityMainActivity extends com.aut.presentationskills.ui.Unity.UnityPlayerActivity {

    private EmotivEventHandler emotivEventHandler;
    private final List<SensorListener> sensorListeners = new ArrayList<>();

    public MqttHelper mqttHelper;
    private Timer timer;

    private String measurementID;
    public Map<String, Float> localPmDatas;

    Intent i;
    public UnityMainActivity() {
        localPmDatas= new HashMap<>();
    }

    // Setup activity layout
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
        //landcpase
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
        startMqtt();

         i = this.getIntent();
        measurementID = i.getStringExtra("measurementID");

        emotivEventHandler = new EmotivEventHandler();

        addSensorListener(emotivEventHandler);

    }

    private void SendUnityMessages(Intent i) {
        String data = i.getStringExtra("setVideoListSize");

        if(data!=null){
            Log.w("And", "Videoliiist!! - " + data);
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setVideoList","https://www.youtube.com/watch?v=ASS9r4ak8CM");
            data=null;
        }
        data = i.getStringExtra("setDeckListSize");
        if(data!=null){
                Log.w("And", "decklistseizeeeee!!");
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setDeckListSize",data);
            data=null;
        }
        data=i.getStringExtra("setDeckList");
        if(data!=null){
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setDeckList",data);
            data=null;
        }
        data=i.getStringExtra("setPositionDeck");
        if(data!=null){
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setPositionDeck",data);
            data=null;
        }
        data = i.getStringExtra("setPromterListSize");
        if(data!=null){
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setPromterListSize",data);
            data=null;
        }
        data=i.getStringExtra("setPromterList");
        if(data!=null){
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setPromterList",data);
            data=null;
        }
        data=i.getStringExtra("setPositionPromter");
        if(data!=null){
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setPositionPromter",data);
            data=null;
        }
    }

    public final void addSensorListener(SensorListener listener) {
        sensorListeners.add(listener);
    }
    public void removeSensorListener(SensorListener listener){
        sensorListeners.remove(listener);
    }

    @Subscribe(threadMode = MAIN)
    public void onSensorStatusChangedEvent(SensorStatusChangedEvent event) {
        for (SensorListener listener : sensorListeners) {
            listener.sensorStatusChanged(event.getName(), event.getStatus());
        }
    }
    @Subscribe(threadMode = MAIN)
    public void onPerformanceMetricChangedEvent(PerformanceMetricUpdatedGameEvent event) {
        for (SensorListener listener : sensorListeners) {
            listener.performanceMetricChanged();
        }
    }

    @Subscribe(threadMode = MAIN)
    public void onVideoUrlChangedEvent(VideoUrlEvent event) {
        for (SensorListener listener : sensorListeners) {
            listener.videoUrlChanged(event.getUrl());
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    //MQTT communication
    private void startMqtt() {
        //try {
            mqttHelper = new MqttHelper(getApplicationContext());
            mqttHelper.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {

                }

                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    if(topic.equals("currentvideo")) {
                        Log.w("mqtt video url changed: ", mqttMessage.toString());
                        onVideoUrlChangedEvent(new VideoUrlEvent(mqttMessage.toString()));
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

    }


    // Quit Unity
    @Override
    protected void onDestroy ()
    {
        mUnityPlayer.quit();
        //removeSensorListener(emotivEventHandler);
        Log.w("test", "destoryed");
        //timer.cancel();
        //timer.purge();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();

    }

    // Resume Unity
    @Override
    protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
            mUnityPlayer.UnitySendMessage("AdaptedEventHandlerMNG", "setSensorStatus", Constants.Status.ONLINE.toString());
    }

    // Low Memory Unity
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);

    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override
    public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

//unity event handlers

    public void sceneLoaded(boolean data){
        if(data){
            Log.w("Android", "Scene loaded, send unity messages!");
            SendUnityMessages(i);
        }
    }

    public void measuerementStarted(boolean data){
        Log.w("Andriod", "get event; measurement started");
        Log.w("Emotiv", Integer.toString(((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().userID));
        if(((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv().userID!=0){
            Timer timer = new Timer();
            EmotivEEG emotiv = ((PresentationSkillsVRandAndroid) this.getApplication()).getEmotiv();
            MqttMessage message = new MqttMessage();
            String startedmessage = "STARTED";
            message.setPayload(startedmessage.getBytes());
            try {
                mqttHelper.mqttAndroidClient.publish("measurement/status", message);

            } catch (MqttException e) {
                e.printStackTrace();
            }

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    localPmDatas = new HashMap<>();
                    MqttMessage message = new MqttMessage();
                    Boolean firstpm = true;

                    for (int key : emotiv.pmGetters.keySet()) {
                        localPmDatas.put(emotiv.pmNames.get(key), emotiv.pmGetters.get(key).getScore(emotiv.pEmoState));
                    }

                    String metricsString = "{ \"measurementId\":  \"" + measurementID + "\"," +
                            "\"data\": [";
                    String onemetric;
                    //Log.w("emotivdata: " ,emotiv.status.toString());
                    for (String key : localPmDatas.keySet()) {
                        //Log.w("key......: ", key);
                        //Log.w("data......: ", Float.toString(localPmDatas.get(key)));
                        if (!firstpm)
                            metricsString += ",";
                        onemetric = "{" + "\"name\": " + "\"" + key + "\"," +
                                "\"value\": " + "\"" + Float.toString(localPmDatas.get(key)) + "\" }";
                        if (firstpm)
                            firstpm = false;
                        metricsString += onemetric;
                    }
                    metricsString += "]}";
                    Log.w("metrics data: ", metricsString);
                    message.setPayload(metricsString.getBytes());

                    try {
                        mqttHelper.mqttAndroidClient.publish("measurement/data", message);

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 3000);//3second
        }
    }

    public void videoChanged(String url){
        //+
        // TODO status changed
        Log.w("Android","video changed message from unity, yess");
    }

    public void measuerementEnded(boolean data){
        Log.w("Android", "measurement end!");
    }

    //Helpers
    public String currentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");  //it will give you the date in the formate that is given in the image
        String datetime = dateformat.format(c.getTime()); // it will give you the date
        return datetime;
    }


}
