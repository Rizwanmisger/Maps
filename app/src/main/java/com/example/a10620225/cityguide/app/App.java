package com.example.a10620225.cityguide.app;

import android.app.Application;

import com.example.a10620225.cityguide.dagger.AppComponent;
import com.example.a10620225.cityguide.dagger.AppModule;
import com.example.a10620225.cityguide.dagger.DaggerAppComponent;

/**
 * Created by 10620225 on 11/20/2017.
 */

public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }
    public AppComponent getAppComponent(){
        return appComponent;
    }

    AppComponent initDagger(App application){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }
}
