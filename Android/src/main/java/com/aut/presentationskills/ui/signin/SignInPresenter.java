package com.aut.presentationskills.ui.signin;

import com.aut.presentationskills.di.Network;
import com.aut.presentationskills.interactor.auth.AccessTokenInteractor;
import com.aut.presentationskills.interactor.auth.event.PostAccessTokenEvent;
import com.aut.presentationskills.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class SignInPresenter extends Presenter<SignInScreen> {
    Executor networkExecutor;
    public String jwt;
    AccessTokenInteractor accessTokenInteractor;

    @Inject
    public SignInPresenter(@Network Executor networkExecutor, AccessTokenInteractor accessTokenInteractor) {
        this.networkExecutor = networkExecutor;
        this.accessTokenInteractor = accessTokenInteractor;
    }
    @Override
    public void attachScreen(SignInScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }
    public void refreshUser(final String accessTokenquery) {
        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                accessTokenInteractor.postAccessToken(accessTokenquery);
            }
        });
        //jwt=accessTokenInteractor.event.getJwt();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final PostAccessTokenEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
            if (screen != null) {
                screen.showNetworkError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                jwt = event.getJwt();
                screen.startMeasurement(jwt);
            }
        }
    }
}
