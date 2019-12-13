package com.aut.presentationskills.network;

import com.aut.presentationskills.model.Deck;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeckApi {
  

  /**
   * Returns a given Deck with \&quot;deckId\&quot;
   * Returns a given Deck with \&quot;deckId\&quot;
   * @param deckId 
   * @return Call<Deck>
   */
  
  @GET("deck/{deckId}")
  Call<Deck> deckDeckIdGet(
          @Path("deckId") String deckId
  );

}
