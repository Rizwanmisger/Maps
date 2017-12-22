package com.example.a10620225.cityguide.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 10620225 on 11/21/2017.
 */

public class PlaceResponse {
    @SerializedName("results")
    List<PlaceDetail> results;

    public List<PlaceDetail> getResults() {
        return results;
    }

    public void setResults(List<PlaceDetail> results) {
        this.results = results;
    }
    /* Results results;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
        }*/


    public class Location{
        @SerializedName("lat")
        Double latitude;
        @SerializedName("lng")
        Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
    public class Geometry{
        @SerializedName("location")
        Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }
    public class PlaceDetail{
        @SerializedName("geometry")
        Geometry geometry;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }
    /*public class Results{
        List<PlaceDetail> placeDetails;

        public List<PlaceDetail> getPlaceDetails() {
            return placeDetails;
        }

        public void setPlaceDetails(List<PlaceDetail> placeDetails) {
            this.placeDetails = placeDetails;
        }
    }*/
}
