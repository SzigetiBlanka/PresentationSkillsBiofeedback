package com.aut.presentationskills;

import android.app.Application;

import com.aut.presentationskills.emotiv.EmotivEEG;
import com.aut.presentationskills.emotiv.SensorModule;
import com.aut.presentationskills.emotiv.util.CommonModule;
import com.aut.presentationskills.ui.UIModule;

public class PresentationSkillsVRandAndroid extends Application {
    public static PresentationSkillsVRandAndroidComponent injector;
    private EmotivEEG emotiv=null;

    @Override
    public void onCreate() {
        super.onCreate();

        injector = DaggerPresentationSkillsVRandAndroidComponent.builder().
                        uIModule(
                                new UIModule(this)
                        ).sensorModule(new SensorModule()).commonModule(new CommonModule(this)).build();
    }

    public void setEmotiv(EmotivEEG emotiv){this.emotiv=emotiv;}

    public EmotivEEG getEmotiv(){return emotiv;}

}
