package com.example.a10620225.cityguide.dagger;

import android.content.Context;


import com.example.a10620225.cityguide.ui.MyMapsPresentation;
import com.example.a10620225.cityguide.ui.MyMapsPresentationImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 10620225 on 11/20/2017.
 */
@Module
public class PresentationModule {
    @Provides
    public MyMapsPresentation provideMapsPresentation(Context context){
        return new MyMapsPresentationImpl(context);
    }
}
