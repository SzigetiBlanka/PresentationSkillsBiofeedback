package com.aut.presentationskills.emotiv;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.aut.presentationskills.di.Emotiv;
import com.aut.presentationskills.emotiv.events.ActivityResultEvent;
import com.aut.presentationskills.emotiv.events.PerformanceMetricUpdatedGameEvent;
import com.aut.presentationskills.emotiv.events.RequestPermissionEvent;
import com.aut.presentationskills.emotiv.util.RequestPermissionActivity;
import com.aut.presentationskills.emotiv.util.ResultActivity;
import com.emotiv.bluetooth.EmotivBluetooth;
import com.emotiv.sdk.edkJavaJNI;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Looper.getMainLooper;
import static com.aut.presentationskills.PresentationSkillsVRandAndroid.injector;
import static com.aut.presentationskills.emotiv.Constants.Status.BAD_SIGNAL;
import static com.aut.presentationskills.emotiv.Constants.Status.CONNECTING;
import static com.aut.presentationskills.emotiv.Constants.Status.OFFLINE;
import static com.aut.presentationskills.emotiv.Constants.Status.ONLINE;
import static com.aut.presentationskills.emotiv.helpers.GsonHelper.gson;
import static com.aut.presentationskills.emotiv.util.RequestPermissionActivity.EXTRA_PERMISSIONS;
import static com.aut.presentationskills.emotiv.util.ResultActivity.EXTRA_ACTION;
import static com.emotiv.sdk.EdkErrorCode.EDK_OK;
import static com.emotiv.sdk.IEE_DataChannels_t.IED_AF3;
import static com.emotiv.sdk.IEE_DataChannels_t.IED_AF4;
import static com.emotiv.sdk.IEE_DataChannels_t.IED_Pz;
import static com.emotiv.sdk.IEE_DataChannels_t.IED_T7;
import static com.emotiv.sdk.IEE_DataChannels_t.IED_T8;
import static com.emotiv.sdk.IEE_Event_t.IEE_EmoStateUpdated;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_ENGAGEMENT;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_EXCITEMENT;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_FOCUS;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_INTEREST;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_RELAXATION;
import static com.emotiv.sdk.IEE_PerformanceMetricAlgo_t.PM_STRESS;
import static com.emotiv.sdk.edkJavaJNI.CustomerSecurity_emotiv_func;
import static com.emotiv.sdk.edkJavaJNI.IEE_CheckSecurityCode;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoEngineEventCreate;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoEngineEventFree;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoEngineEventGetEmoState;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoEngineEventGetType;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoEngineEventGetUserId;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoStateCreate;
import static com.emotiv.sdk.edkJavaJNI.IEE_EmoStateFree;
import static com.emotiv.sdk.edkJavaJNI.IEE_EngineConnect__SWIG_0;
import static com.emotiv.sdk.edkJavaJNI.IEE_EngineDisconnect;
import static com.emotiv.sdk.edkJavaJNI.IEE_EngineGetNextEvent;
import static com.emotiv.sdk.edkJavaJNI.IEE_GetSecurityCode;
import static com.emotiv.sdk.edkJavaJNI.IS_PerformanceMetricIsActive;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class EmotivEEG implements IEmotivEEG {

    private static final String TAG = "EmotivEEG";

    private static final String DEV_ID = "EmotivApp-android";
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final int BLUETOOTH_REQUEST_CODE = 1;
    private static final long CONNECT_TIMEOUT = 10000L;
    private static final long CONNECT_PERIOD = 10L;
    private static final long POLL_PERIOD = 1000L;

    @Inject
    @Emotiv
    Handler backgroundHandler;

    @Inject
    Context context;

    //@Inject
    //EventBus bus;

    public final Handler handler;
    public final Map<Integer, String> pmNames;
    public final Map<Integer, PerformanceMetricGetter> pmGetters;
    public final Map<Integer, Float> performanceMetrics;
    public final Map<Integer, Long> pmUpdateTimes;

    public volatile Constants.Status status = OFFLINE;
    public long connectStartTime;
    public boolean engineConnected;
    public long pEvent;
    public long pEmoState;
    public int userID = 0;
    public volatile boolean deviceConnected;

    public boolean connectfail;

    int channelList[] = {IED_AF3, IED_AF4, IED_T7, IED_T8, IED_Pz};

    double bands[][] =
            {{0.0, 0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0, 0.0}};


    public interface PerformanceMetricGetter {
        float getScore(long hEmoState);
    }

    public EmotivEEG() {
        injector.inject(this);

        handler = new Handler(getMainLooper());
        pmNames = Collections.unmodifiableMap(createPmNames());
        pmGetters = Collections.unmodifiableMap(createPmGetters());
        performanceMetrics = new HashMap<>();
        pmUpdateTimes = new HashMap<>();
    }

    private static Map<Integer, String> createPmNames() {
        Map<Integer, String> pmNames = new HashMap<>();
        pmNames.put(PM_EXCITEMENT, "excitement");
        pmNames.put(PM_RELAXATION, "relaxation");
        pmNames.put(PM_STRESS, "stress");
        pmNames.put(PM_ENGAGEMENT, "engagement");
        pmNames.put(PM_INTEREST, "interest");
        pmNames.put(PM_FOCUS, "focus");
        return pmNames;
    }

    private static Map<Integer, PerformanceMetricGetter> createPmGetters() {
        Map<Integer, PerformanceMetricGetter> pmGetters = new HashMap<>();
        pmGetters.put(PM_EXCITEMENT,
                edkJavaJNI::IS_PerformanceMetricGetInstantaneousExcitementScore);
        pmGetters.put(PM_RELAXATION, edkJavaJNI::IS_PerformanceMetricGetRelaxationScore);
        pmGetters.put(PM_STRESS, edkJavaJNI::IS_PerformanceMetricGetStressScore);
        pmGetters.put(PM_ENGAGEMENT, edkJavaJNI::IS_PerformanceMetricGetEngagementBoredomScore);
        pmGetters.put(PM_INTEREST, edkJavaJNI::IS_PerformanceMetricGetInterestScore);
        pmGetters.put(PM_FOCUS, edkJavaJNI::IS_PerformanceMetricGetFocusScore);
        return pmGetters;
    }

    private boolean checkPermission() {
        if(SDK_INT >= M) {
            boolean permissionGranted = ContextCompat
                    .checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;

            if (!permissionGranted) {
                Intent intent = new Intent(context, RequestPermissionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(RequestPermissionActivity.EXTRA_REQUEST_CODE,
                        PERMISSION_REQUEST_CODE);
                intent.putExtra(EXTRA_PERMISSIONS, new String[]{ACCESS_FINE_LOCATION});
                context.startActivity(intent);
            }

            return permissionGranted;
        }

        return true;
    }

    private boolean checkBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        boolean bluetoothEnabled = bluetoothAdapter != null && bluetoothAdapter.isEnabled();

        if (!bluetoothEnabled) {
            Intent intent = new Intent(context, ResultActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(EXTRA_ACTION, BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.putExtra(ResultActivity.EXTRA_REQUEST_CODE, BLUETOOTH_REQUEST_CODE);
            context.startActivity(intent);
            Log.w("Bluetooth disabled!", "");
        }

        return bluetoothEnabled;
    }

    @Override
    public void connect() {
        Log.w(TAG, status.toString());
        if (status != OFFLINE) {
            return;
        }

        status = CONNECTING;
        sendStatus();

        //bus.register(this);
        if(checkPermission()){
            Log.w(TAG, "permission checked true");
        }else
            Log.w(TAG, "permission checked false");
        if(checkBluetooth()){
            Log.w(TAG, "bluetooth checked true");
        }else
            Log.w(TAG, "bluetooth checked false");


        if (checkPermission() && checkBluetooth()) {
            connectEngine();
        }
    }

    private void connectEngine() {
        if (IEE_EngineConnect__SWIG_0(DEV_ID) != EDK_OK) {
            Log.w(TAG, "engine connect failed");
            disconnect();
            return;
        }

        Log.w(TAG, "engine connect ok");

        if (IEE_CheckSecurityCode(CustomerSecurity_emotiv_func(IEE_GetSecurityCode())) != EDK_OK) {
            Log.w(TAG, "security code check failed");
            disconnect();
            return;
        }

        Log.w(TAG, "security code check ok");

        engineConnected = true;
        pEvent = IEE_EmoEngineEventCreate();
        pEmoState = IEE_EmoStateCreate();

        if (EmotivBluetooth._emobluetooth == null) {
            EmotivBluetooth._emobluetooth = new EmotivBluetooth(context);
        }

        connectStartTime = SystemClock.uptimeMillis();
        backgroundHandler.post(this::connectDevice);
    }

    private void connectDevice() {
        if (status != CONNECTING) {
            return;
        }

        int deviceCount = EmotivBluetooth._emobluetooth.GetNumberDeviceInsight();

        if (deviceCount == 0 ||
                !EmotivBluetooth._emobluetooth.EmoConnectDevice(0, deviceCount - 1)) {
            if (SystemClock.uptimeMillis() - connectStartTime < CONNECT_TIMEOUT) {
                backgroundHandler.postDelayed(this::connectDevice, CONNECT_PERIOD);
            } else {
                connectfail = true;
                Log.w(TAG, "device connect failed");
                handler.post(this::disconnect);
            }

            return;
        }

        Log.w(TAG, "device connect ok");

        deviceConnected = true;
        status = ONLINE;
        sendStatus();

        handleEvents();
    }

    private void handleEvents() {
        if (status != ONLINE && status != BAD_SIGNAL) {
            return;
        }

        long timestamp = System.currentTimeMillis();

        while (IEE_EngineGetNextEvent(pEvent) == EDK_OK) {
            int type = IEE_EmoEngineEventGetType(pEvent);
            IEE_EmoEngineEventGetUserId(pEvent, userID);

            Log.w("Insight", String.valueOf(type));

            if (type == IEE_EmoStateUpdated) {
                int getEmoStateResult = IEE_EmoEngineEventGetEmoState(pEvent, pEmoState);

                if (getEmoStateResult == EDK_OK) {
                    //noinspection Convert2streamapi
                    for (int key : pmGetters.keySet()) {
                        updatePerformanceMetric(key, timestamp);
                        Log.w(TAG, "name: " + pmNames.get(key) + "score: " + pmGetters.get(key).getScore(pEmoState));
                    }
                }
            }
        }

        boolean badSignal = false;

        Map<String, Float> namedMetrics = new HashMap<>();

        for (
                int key : pmGetters.keySet())

        {


            namedMetrics.put(pmNames.get(key), performanceMetrics.get(key));

            long updateTime = pmUpdateTimes.containsKey(key) ? pmUpdateTimes.get(key) : 0;

            if (!badSignal && timestamp - updateTime > 0) {
                badSignal = true;
            }
        }

        PerformanceMetricUpdatedGameEvent event = new PerformanceMetricUpdatedGameEvent(gson.toJson(namedMetrics));
        //bus.post(new FrameworkEvent(event, timestamp, false));

        //PerformanceMetricUpdatedSvmEvent svmEvent = new PerformanceMetricUpdatedSvmEvent(namedMetrics);
        //bus.post(new FrameworkEvent(svmEvent, timestamp, true));*/

        Constants.Status prevStatus = status;
        status = badSignal ? BAD_SIGNAL : ONLINE;

        if (status != prevStatus)

        {
            sendStatus();
        }

        backgroundHandler.postDelayed(this::handleEvents, POLL_PERIOD);
    }

    private void updatePerformanceMetric(int key, long timestamp) {
        if (IS_PerformanceMetricIsActive(pEmoState, key) == 1) {
            //Float oldScore = performanceMetrics.get(key);
            float newScore = pmGetters.get(key).getScore(pEmoState);
            performanceMetrics.put(key, newScore);
            pmUpdateTimes.put(key, timestamp);

            /*
            if (oldScore == null || oldScore != newScore) {
                PerformanceMetricUpdatedGameEvent event =
                        new PerformanceMetricUpdatedGameEvent(pmNames.get(key), newScore);
                bus.post(new FrameworkEvent(event, timestamp, true));

                Log.d(TAG, event.getMetric() + ": " + event.getScore());
            }
            */
        }
    }

    @Override
    public void disconnect() {
        if (status == OFFLINE) {
            return;
        }

        status = OFFLINE;
        sendStatus();

        backgroundHandler.removeCallbacksAndMessages(null);

        if (deviceConnected) {
            EmotivBluetooth._emobluetooth.DisconnectHeadset();
            deviceConnected = false;
        }

        if (pEvent != 0) {
            IEE_EmoEngineEventFree(pEvent);
            pEvent = 0;
        }

        if (pEmoState != 0) {
            IEE_EmoStateFree(pEmoState);
            pEmoState = 0;
        }

        if (engineConnected) {
            IEE_EngineDisconnect();
            engineConnected = false;
        }

        //bus.unregister(this);
    }

    @Subscribe(threadMode = MAIN)
    public void onRequestPermissionEvent(RequestPermissionEvent event) {
        if (event.getRequestCode() == PERMISSION_REQUEST_CODE) {
            if (event.hasGranted(ACCESS_FINE_LOCATION)) {
                if (checkBluetooth()) {
                    connectEngine();
                }
            } else {
                disconnect();
            }
        }
    }

    @Subscribe(threadMode = MAIN)
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (event.getRequestCode() == BLUETOOTH_REQUEST_CODE) {
            if (event.getResultCode() == RESULT_OK) {
                connectEngine();
            } else {
                disconnect();
            }
        }
    }

    @Override
    public void sendStatus() {
        //bus.post(new SensorEvent(Constants.Sensors.EmotivEEG.ACTION_RESPONSE_STATUS, status));
    }

}
