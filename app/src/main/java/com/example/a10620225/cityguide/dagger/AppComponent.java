package com.example.a10620225.cityguide.dagger;



import com.example.a10620225.cityguide.ui.MyMapActivity;
import com.example.a10620225.cityguide.ui.MyMapsPresentationImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 10620225 on 11/20/2017.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class, PresentationModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(MyMapsPresentationImpl target);
    void inject(MyMapActivity target);
}
