package com.example.a10620225.cityguide.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 10620225 on 11/20/2017.
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application = application;
    }
    @Singleton
    @Provides
    public Context provideContext(){
        return application;
    }
}
