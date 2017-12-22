package com.example.a10620225.cityguide.ui;


import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by 10620225 on 11/20/2017.
 */

public interface MyMapsPresentation {
    public GoogleApiClient getGoogleApiClient();
    public void setView(MyMapsView view);

    void getNearby(Location location, String radius,String type, String key);
}
