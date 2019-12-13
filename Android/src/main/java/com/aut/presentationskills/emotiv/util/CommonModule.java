package com.aut.presentationskills.emotiv.util;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.aut.presentationskills.di.Network;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

@Module
public class CommonModule {
    private static final String NETWORK_THREAD_NAME = "networkThread";

    private final Context context;

    public CommonModule(Context context) {
        this.context = context;
    }

    @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public Executor provideExecutor() {
        return Executors.newFixedThreadPool(1);
    }

    private Looper newLooper(String threadName) {
        HandlerThread thread = new HandlerThread(threadName, THREAD_PRIORITY_BACKGROUND);
        thread.start();
        return thread.getLooper();
    }

    @Provides
    @Singleton
    @Network
    public Looper provideNetworkLooper() {
        return newLooper(NETWORK_THREAD_NAME);
    }


    @Provides
    @Network
    public Handler provideNetworkHandler(@Network Looper looper) {
        return new Handler(looper);
    }

}
