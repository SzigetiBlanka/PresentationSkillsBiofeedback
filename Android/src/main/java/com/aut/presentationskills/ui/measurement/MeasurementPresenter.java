package com.aut.presentationskills.ui.measurement;


import android.util.Log;

import com.aut.presentationskills.di.Network;
import com.aut.presentationskills.interactor.user.UserInteractor;
import com.aut.presentationskills.interactor.user.event.GetMeasurementEvent;
import com.aut.presentationskills.interactor.user.event.GetUserEvent;
import com.aut.presentationskills.model.MeasurementStatus;
import com.aut.presentationskills.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class MeasurementPresenter extends Presenter<MeasurementScreen> {
    Executor networkExecutor;
    UserInteractor userInteractor;


    @Inject
    public MeasurementPresenter(@Network Executor networkExecutor, UserInteractor userInteractor) {
        this.networkExecutor = networkExecutor;
        this.userInteractor = userInteractor;
    }
    @Override
    public void attachScreen(MeasurementScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void refreshUser(final String userQuery) {
        if( userQuery.equals("")){
            networkExecutor.execute(() -> userInteractor.getUser());
        }else{
        networkExecutor.execute(() -> userInteractor.getUserwithID(userQuery));}
    }
    public void getMeasurementData(final String userIdQuery){
        if( !userIdQuery.equals("")){
            Log.w("userid: ", userIdQuery);
            networkExecutor.execute(() -> userInteractor.getUserMeasurements(userIdQuery, String.valueOf(MeasurementStatus.ASSIGNED)));}
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final GetUserEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
            if (screen != null) {
                screen.showNetworkError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                //TODO:userid-val lekérni a measurement adatokat
                Log.w("megvan a sajat useradatom: ", event.getUser().getEmail());
                screen.getMeasurementDataswithUserId(event.getUser());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final GetMeasurementEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
            if (screen != null) {
                screen.showNetworkError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                //TODO:measurement adatokat megjeleníteni
                boolean haveMeasurement = false;
                for(int i=0; i < event.getMeasurement().size(); i++){
                    if(event.getMeasurement().get(i).getStatus()== MeasurementStatus.ASSIGNED){
                        screen.showMeasurementDatas(event.getMeasurement().get(i));
                        haveMeasurement=true;
                    }
                }
                if(!haveMeasurement){
                    screen.loadAgainMeasurementDatay();
                }
                //screen.showMeasurementDatas(event.getMeasurement().get(0));
            }
        }
    }
}
