package com.aut.presentationskills.interactor.user;

import android.util.Log;

import com.aut.presentationskills.PresentationSkillsVRandAndroid;
import com.aut.presentationskills.interactor.user.event.GetMeasurementEvent;
import com.aut.presentationskills.interactor.user.event.GetUserEvent;
import com.aut.presentationskills.model.Measurement;
import com.aut.presentationskills.model.User;
import com.aut.presentationskills.network.NetworkConfig;
import com.aut.presentationskills.network.UserApi;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class UserInteractor {
    UserApi userapi;
    String jwt;

    @Inject
    public UserInteractor(UserApi userapi){
    this.userapi= userapi;
    PresentationSkillsVRandAndroid.injector.inject(this);
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }

    public void getUser(){
        GetUserEvent event = new GetUserEvent();
        try{
            String authToken = NetworkConfig.AUTH_PREFIX+ jwt;
            Log.w("Authorization: ", authToken);
            Call<User> userQueryCall = userapi.userMeGet(authToken);

            Response<User> response = userQueryCall.execute();
            if (response.code() != 200) {
                throw new Exception("Result code is not 200");
            }
            event.setUser(response.body());
            Log.w("current user id: ", response.body().getId());
            Log.w("current user email: ", response.body().getEmail());
            EventBus.getDefault().post(event);
        }catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }
    public void getUserwithID(String userid){
        GetUserEvent event = new GetUserEvent();
        try{
            Call<User> userqueryCall = userapi.userUserIdGet(jwt, userid);

            Response<User> response = userqueryCall.execute();
            if (response.code() != 200) {
                throw new Exception("Result code is not 200");
            }
            event.setUser(response.body());
            EventBus.getDefault().post(event);
        }catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }

    public void getUserMeasurements(String userId, String status){
        GetMeasurementEvent event = new GetMeasurementEvent();
        try{
            String authToken = NetworkConfig.AUTH_PREFIX + jwt;
            Call<List<Measurement>> measurementsCall = userapi.userUserIdMeasurementsGet(authToken, userId, "");

            Response<List<Measurement>> response = measurementsCall.execute();
            if (response.code() != 200) {
                throw new Exception("Result code is not 200. It is: " + response.code());
            }
            event.setMeasurement(response.body());
            EventBus.getDefault().post(event);
        }catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }

}
