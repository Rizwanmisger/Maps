package com.example.a10620225.cityguide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by 10620225 on 11/20/2017.
 */

public class GoogleApiClientHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionListener connectionListener;
    private Bundle connectionBundle;

    public GoogleApiClientHelper(Context context){
        this.context = context;
        buildGoogleApiClient(context);
        connect();
    }
    public void setConnectionListener(ConnectionListener connectionListener){
        this.connectionListener = connectionListener;
        if (this.connectionListener != null && isConnected()) {
            connectionListener.onConnected(connectionBundle);
        }
    }
    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }
    private void buildGoogleApiClient(Context context){
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.connectionBundle = bundle;
        if(connectionListener != null){
            connectionListener.onConnected(bundle);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        if (connectionListener != null) {
            connectionListener.onConnectionSuspended(i);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionListener != null) {
            connectionListener.onConnectionFailed(connectionResult);
        }
    }

    private void connect(){
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }


    public interface ConnectionListener{
        void onConnected(Bundle bundle);
        void onConnectionSuspended(int i);
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }
}
