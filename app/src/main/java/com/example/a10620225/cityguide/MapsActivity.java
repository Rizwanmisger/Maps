package com.example.a10620225.cityguide;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.util.List;


public class MapsActivity extends FragmentActivity implements MapsActivityView
        {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    final MapsPresentationImpl presentation = new MapsPresentationImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fob = (FloatingActionButton)findViewById(R.id.fab);
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentation.loadPlacePicker(MapsActivity.this, PLACE_PICKER_REQUEST);
            }
        });
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        createLocationRequest();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng newYork = new LatLng(40.73, -73.99);
        mMap.addMarker(new MarkerOptions().position(newYork).title("Marker in New York"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 12));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
        if (mLocationUpdateState) {

           presentation.startLocationUpdates(this, mGoogleApiClient, mLocationRequest, this, REQUEST_CHECK_SETTINGS );
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (null != mLastLocation) {
            placeMarkerOnMap(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }
    private void setUpMap() {
       if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
              return;
        }

        mMap.setMyLocationEnabled(true);
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }
        }
    }
    protected void placeMarkerOnMap(LatLng location){
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        String titleStr = getAddress(location);
        markerOptions.title(titleStr);

        mMap.addMarker(markerOptions);
    }
    private String getAddress( LatLng latLng ) {
        Geocoder geocoder = new Geocoder(this);
        String addressText = "er";
        List<Address> addresses;
        Address address;
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if(addresses != null && !addresses.isEmpty()){
                    address = addresses.get(0);
                    for(int i = 0; i <  address.getMaxAddressLineIndex(); i++){
                        addressText += (i == 0)?(address.getAddressLine(i)):("\n" + address.getAddressLine(i));
                    }
                }
            }catch(Exception e){

            }
            return addressText;
    }

    /*protected void startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }*/
    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>(){
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch(status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:{
                        mLocationUpdateState = true;
                       // startLocationUpdates();
                        presentation.startLocationUpdates(MapsActivity.this, mGoogleApiClient, mLocationRequest, MapsActivity.this, REQUEST_CHECK_SETTINGS );
                    }
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:{
                        try {
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CHECK_SETTINGS){
            if(resultCode == RESULT_OK){
                mLocationUpdateState = true;
              //  startLocationUpdates();
                presentation.startLocationUpdates(this, mGoogleApiClient, mLocationRequest, this, REQUEST_CHECK_SETTINGS );
            }
        }
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                String addressText = place.getName().toString();
                addressText += "\n" + place.getAddress().toString();

                placeMarkerOnMap(place.getLatLng());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            //startLocationUpdates();
            presentation.startLocationUpdates(this, mGoogleApiClient, mLocationRequest, this, REQUEST_CHECK_SETTINGS );
        }
    }

   /* protected void loadPlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
                startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
        }catch(GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){

        }
    }*/
}
