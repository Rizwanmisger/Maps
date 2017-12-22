package com.example.a10620225.cityguide.ui;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.a10620225.cityguide.GoogleApiClientHelper;
import com.example.a10620225.cityguide.app.App;
import com.example.a10620225.cityguide.model.PlaceResponse;
import com.example.a10620225.cityguide.networking.PlacesApi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 10620225 on 11/20/2017.
 */

public class MyMapsPresentationImpl implements MyMapsPresentation {

    @Inject
    public GoogleApiClientHelper googleApiClientHelper;
    @Inject
    public PlacesApi placesApi;
    MyMapsView view;

    public MyMapsPresentationImpl(Context context){

        ((App)context).getAppComponent().inject(this);
    }
    @Override
    public void setView(MyMapsView view) {
        this.view = view;
    }
    @Override
    public GoogleApiClient getGoogleApiClient() {
       googleApiClientHelper.setConnectionListener(new GoogleApiClientHelper.ConnectionListener() {
           @Override
           public void onConnected(Bundle bundle) {
                view.setUpMap();
           }

           @Override
           public void onConnectionSuspended(int i) {

           }

           @Override
           public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

           }
       });

       return googleApiClientHelper.getGoogleApiClient();
    }
    @Override
    public void getNearby(Location location, String radius,  String type,String key){

        Log.v("LocationList" , "==method called");
        String locationString = location.getLatitude() + "," + location.getLongitude();

      //  locationString = "40.73" +","+ "-73.99";
        placesApi.getNearbyRestaurants(locationString, radius,type, key).enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                Log.v("LocationList" , "==Success");
                ArrayList<PlaceResponse.Location> locationList = new ArrayList<>();
                List<PlaceResponse.PlaceDetail> results = response.body().getResults();

                for(PlaceResponse.PlaceDetail detail : results){
                    PlaceResponse.Geometry geometry = detail.getGeometry();
                    PlaceResponse.Location location = geometry.getLocation();
                    locationList.add(location);
                }

                Log.v("LocationList" , new Integer(locationList.size()).toString());
                view.showNearbyRestaurants(locationList);
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                Log.v("LocationList" , "==Failure");
               throw new NullPointerException();

            }
        });
    }

}
