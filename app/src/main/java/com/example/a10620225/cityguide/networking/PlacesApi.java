package com.example.a10620225.cityguide.networking;

import com.example.a10620225.cityguide.model.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 10620225 on 11/22/2017.
 */

public interface PlacesApi {
    final static String API_KEY = "AIzaSyBMwtsqtdfaIoJSFPiNxACKRf9zIfPPqgU";

    @GET("/maps/api/place/nearbysearch/json")
    Call<PlaceResponse> getNearbyRestaurants(@Query("location") String location, @Query("radius") String radius,@Query("type") String type, @Query("key") String key);
}
