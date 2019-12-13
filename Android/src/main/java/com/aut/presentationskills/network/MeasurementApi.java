package com.aut.presentationskills.network;

import com.aut.presentationskills.model.Measurement;
import com.aut.presentationskills.model.Scene;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MeasurementApi {
  /**
   * Returns a single Measurement by Id
   * Returns a single Measurement by Id. Available for (Admins, own Subject)
   * @param measurementId 
   * @return Call<Measurement>
   */
  
  @GET("measurement/{measurementId}")
  Call<Measurement> measurementMeasurementIdGet(
          @Path("measurementId") String measurementId
  );


  /**
   * All Scenes of a Measurement by Id
   * All Scenes of a Measurement by Id
   * @param measurementId
   * @return Call<Scene>
   */

  @GET("measurement/{measurementId}/scene")
  Call<Scene> measurementMeasurementIdSceneGet(
          @Path("measurementId") String measurementId
  );

  /**
   * All Scenes of a Measurement by Id
   * All Scenes of a Measurement by Id
   * @param measurementId
   * @return Call<>
   */

  /*@PATCH("/measurement/{measurementId}/status")
  Call<> measurementMeasurementIdStatusPatch(
          @Query("measurementId") String measurementId
  );*/

}
