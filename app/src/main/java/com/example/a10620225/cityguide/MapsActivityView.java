package com.example.a10620225.cityguide;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by 10620225 on 11/17/2017.
 */

public  interface MapsActivityView extends OnMapReadyCallback
        ,GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMarkerClickListener
        , LocationListener {


}
