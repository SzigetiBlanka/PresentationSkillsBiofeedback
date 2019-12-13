package com.aut.presentationskills;

import com.aut.presentationskills.emotiv.EmotivEEG;
import com.aut.presentationskills.emotiv.SensorModule;
import com.aut.presentationskills.emotiv.util.CommonModule;
import com.aut.presentationskills.emotiv.util.RequestPermissionActivity;
import com.aut.presentationskills.emotiv.util.ResultActivity;
import com.aut.presentationskills.interactor.auth.AccessTokenInteractor;
import com.aut.presentationskills.interactor.user.UserInteractor;
import com.aut.presentationskills.network.NetworkModule;
import com.aut.presentationskills.ui.UIModule;
import com.aut.presentationskills.ui.measurement.MeasurementActivity;
import com.aut.presentationskills.ui.signin.SignInActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {UIModule.class, NetworkModule.class, SensorModule.class, CommonModule.class})
public interface PresentationSkillsVRandAndroidComponent{
    void inject(AccessTokenInteractor accessTokenInteractor);

    void inject(UserInteractor userInteractor);

    void inject(SignInActivity signInActivity);

    //void inject(SignInPresenter signInPresenter);
    void inject(MeasurementActivity measurementActivity);

    void inject(EmotivEEG emotivEEG);

    void inject(RequestPermissionActivity requestPermissionActivity);

    void inject(ResultActivity resultActivity);

    //void inject(MeasurementPresenter measurementPresenter);
}
