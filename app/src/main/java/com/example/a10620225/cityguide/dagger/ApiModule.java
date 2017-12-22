package com.example.a10620225.cityguide.dagger;

import android.content.Context;

import com.example.a10620225.cityguide.GoogleApiClientHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 10620225 on 11/20/2017.
 */
@Module
public class ApiModule {

    @Provides
    public GoogleApiClientHelper provideApiClientHelper(Context context){
        return new GoogleApiClientHelper(context);
    }
}
