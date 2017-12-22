package com.example.a10620225.cityguide.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.a10620225.cityguide.R;
import com.example.a10620225.cityguide.app.App;
import com.example.a10620225.cityguide.model.PlaceResponse;
import com.example.a10620225.cityguide.networking.PlacesApi;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 10620225 on 11/20/2017.
 */

public class MyMapActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MyMapsView,NavigationView.OnNavigationItemSelectedListener {

    @Inject
    public MyMapsPresentation presentation;

    private GoogleMap mMap;
    private FloatingActionButton fob;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;

    Activity activity = this;
    final static int LOCATION_PERMISSION_REQUEST_CODE = 1;
     final static int PLACE_PICKER_REQUEST_CODE = 2;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void showNearbyRestaurants(ArrayList<PlaceResponse.Location> locations) {

        for(PlaceResponse.Location location : locations){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            String titleStr = getAddress(latLng);
            markerOptions.title(titleStr);
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = new com.google.android.gms.maps.SupportMapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content_container, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);




        ((App)getApplication()).getAppComponent().inject(this);
        presentation.setView(this);
        mGoogleApiClient = presentation.getGoogleApiClient();
    }

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
    public boolean onMarkerClick(Marker marker) {

        LatLng position = marker.getPosition();
        Toast.makeText(activity, new Double (position.latitude).toString(),
                Toast.LENGTH_LONG).show();

        LatLng source = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        LatLng destination = new LatLng(position.latitude, position.longitude);
        drawRoute(source, destination);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_PICKER_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                String addressText = place.getName().toString();
                addressText += "\n" + place.getAddress().toString();

                placeMarkerOnMap(place.getLatLng());

            }
        }
    }

    @Override
    public void setUpMap() {
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
              //  placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                //presentation.getNearbyRestaurants(mLastLocation, "5000", PlacesApi.API_KEY);
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
        String addressText = "";
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

    private void loadPlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST_CODE);
        }catch(GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){

        }
    }

    private void drawRoute(LatLng source, LatLng destination ){
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(source) // Point A.
                .add(destination); // Point B.

        Polyline polyline = mMap.addPolyline(polylineOptions);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_restaurant) {
            String radius = "5000";
            String type = "restaurant";
            mMap.clear();
            presentation.getNearby(mLastLocation,radius, type,  PlacesApi.API_KEY);
        }

        else if(id == R.id.nav_hospital){
            String radius = "5000";
            String type = "hospital";
            mMap.clear();
            presentation.getNearby(mLastLocation,radius, type,  PlacesApi.API_KEY);
        }

        else if(id == R.id.nav_search){
            mMap.clear();
            loadPlacePicker();
        }

        else if(id == R.id.nav_fragment){
            MapFragment fragment = new MapFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.content_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
