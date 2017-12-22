package com.example.a10620225.cityguide;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 10620225 on 11/17/2017.
 */

@Module
public class MapModule {
    @Singleton
    @Provides
    GoogleApiClient getGoogleApiClient(Application context){
        return new GoogleApiClient.Builder(context).build();
    }

    @Singleton
    @Provides
    LocationRequest getLocationRequest(){
        return new LocationRequest();
    }
}
