package com.example.a10620225.cityguide.ui;

import com.example.a10620225.cityguide.model.PlaceResponse;

import java.util.ArrayList;

/**
 * Created by 10620225 on 11/20/2017.
 */

public interface MyMapsView {
    void setUpMap();
    void showNearbyRestaurants(ArrayList<PlaceResponse.Location> locations);
}
