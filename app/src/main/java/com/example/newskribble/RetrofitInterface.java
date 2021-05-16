package com.example.newskribble;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET("/user//2529//flow")
    Call<MusicModel> getAllData();
}
