package com.example.a10620225.cityguide;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by 10620225 on 11/17/2017.
 */

public interface MapsPresentation {
    void loadPlacePicker(Activity activity, int requestCode);
    public void startLocationUpdates(Activity activity,GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, int requestCode);
}
