package com.fit5046.wildsecured.GoogleModel;

public class SharedPlaceModel {

    String lat;
    String lon;
    String address;
    String name;

    public SharedPlaceModel(String lat, String lon, String address, String name) {
        this.lat = lat;
        this.lon = lon;
        this.address = address;
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
