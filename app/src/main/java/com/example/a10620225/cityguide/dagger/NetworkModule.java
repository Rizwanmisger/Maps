package com.example.a10620225.cityguide.dagger;

import com.example.a10620225.cityguide.networking.PlacesApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 10620225 on 11/22/2017.
 */
@Module
public class NetworkModule {
    private static final String NAME_BASE_URL = "NAME_BASE_URL";
    private static final String url = "https://maps.googleapis.com";
    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrl(){return url;}

    @Singleton
    @Provides
    Converter.Factory provideConverter(){return GsonConverterFactory.create();}

    @Singleton
    @Provides
    Retrofit provideRetrofit( Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl){
        return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(converter)
                    .build();
    }

    @Provides
    PlacesApi providePlacesApi(Retrofit retrofit){
        return retrofit.create(PlacesApi.class);
    }

}
