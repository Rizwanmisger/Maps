package com.example.a10620225.cityguide;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by 10620225 on 11/17/2017.
 */

public class MapsPresentationImpl implements MapsPresentation {
    @Override
    public void loadPlacePicker(Activity activity, int requestCode) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            activity.startActivityForResult(builder.build(activity), requestCode);
        }catch(GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){

        }
    }

    @Override
    public void startLocationUpdates(Activity activity,GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCode);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
    }
}
