package com.webxert.seefgouser.models;

/**
 * Created by hp on 4/14/2019.
 */

public class DriverLatLng {
    double latitude;
    double longitude;

    public DriverLatLng() {
    }

    public DriverLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
