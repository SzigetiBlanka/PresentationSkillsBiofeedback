package com.aut.presentationskills.network;

import com.aut.presentationskills.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VideoApi {

  /**
   * Returns a Video by Id
   * Returns a Video by Id
   * @param videoId 
   * @return Call<Video>
   */
  
  @GET("video/{videoId}")
  Call<Video> videoVideoIdGet(
          @Path("videoId") String videoId
  );

  
}
