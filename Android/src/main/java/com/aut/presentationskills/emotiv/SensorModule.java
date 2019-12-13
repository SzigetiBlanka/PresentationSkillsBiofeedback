package com.aut.presentationskills.emotiv;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.aut.presentationskills.di.Emotiv;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

@Module
public class SensorModule {
    private static final String EMOTIV_THREAD_NAME = "emotivThread";

    private Looper newLooper(String threadName) {
        HandlerThread thread = new HandlerThread(threadName, THREAD_PRIORITY_BACKGROUND);
        thread.start();
        return thread.getLooper();
    }

    @Provides
    @Singleton
    @Emotiv
    public Looper provideEmotivLooper() {
        return newLooper(EMOTIV_THREAD_NAME);
    }

    @Provides
    @Emotiv
    public Handler provideEmotivHandler(@Emotiv Looper looper) {
        return new Handler(looper);
    }

    @Provides
    @Singleton
    public IEmotivEEG provideEmotivEEG() {
        return new EmotivEEG();
    }
}
