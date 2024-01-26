package com.example.gameonline.api;

import com.example.gameonline.model.ConnectRequest;
import com.example.gameonline.model.Game;
import com.example.gameonline.model.GamePlay;
import com.example.gameonline.model.Player;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("start")
    Call<Game> start(@Body Player player);
    @POST("connect")
    Call<Game> connect(@Body ConnectRequest connectRequest);
    @POST("connect/random")
    Call<Game> connectRandom(@Body Player player);
    @POST("gameplay")
    Call<Game> play(@Body GamePlay gamePlay);
}
