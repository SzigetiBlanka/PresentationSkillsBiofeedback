package com.aut.presentationskills.interactor.auth;

import android.util.Log;

import com.aut.presentationskills.PresentationSkillsVRandAndroid;
import com.aut.presentationskills.interactor.auth.event.PostAccessTokenEvent;
import com.aut.presentationskills.model.Jwt;
import com.aut.presentationskills.network.AuthApi;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;


public class AccessTokenInteractor {
    private AuthApi authApi;
    public PostAccessTokenEvent event;
    @Inject
    public AccessTokenInteractor(AuthApi authApi){
        this.authApi= authApi;
        PresentationSkillsVRandAndroid.injector.inject(this);
    }

    public void postAccessToken(String accessTokenquery){
        event = new PostAccessTokenEvent();
        try{
            Call<Jwt> authqueryCall = authApi.accessTokenPost(accessTokenquery);

            Response<Jwt> response = authqueryCall.execute();
            if (response.code() != 200) {
                throw new Exception("Result code is not 200");
            }
            event.setJwt(response.body().getJwt());
            Log.w("------jwt: ", response.body().getJwt());
            EventBus.getDefault().post(event);
        }catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }
}
