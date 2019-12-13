package com.aut.presentationskills.network;

import com.aut.presentationskills.model.Jwt;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface AuthApi {
    /**
     * Creates a new Video
     * Creates a new Video
     * @return Call<Jwt>
     */

    @POST("auth/google")
    Call<Jwt> accessTokenPost(@Query("access_token") String accesstoken);

}
