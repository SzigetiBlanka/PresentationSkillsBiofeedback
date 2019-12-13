package com.aut.presentationskills.network;

import com.aut.presentationskills.model.Measurement;
import com.aut.presentationskills.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

  
  /**
   * Info about the current logged in user
   * 
   * @return Call<User>
   */
  @GET("user/me")
  Call<User> userMeGet(@Header("Authorization") String jwt);
    

  
  /**
   * Returns a single User by \&quot;userId\&quot;
   * Returns a single User by \&quot;userId\&quot;
   * @param userId 
   * @return Call<User>
   */
  
  @GET("user/{userId}")
  Call<User> userUserIdGet(
          @Header("Authorization") String jwt,
          @Path("userId") String userId
  );


  
  /**
   * Measurements of a given User
   * Returns all Measurements of a given User. Query by measurementStatus. Available for (Admins, own Subject)
   * @param userId 
   * @param status 
   * @return Call<Measurement>
   */
  
  @GET("user/{userId}/measurements")
  Call<List<Measurement>> userUserIdMeasurementsGet(
          @Header("Authorization") String jwt,
          @Path("userId") String userId,
          @Query("status") String status
  );

  
}
