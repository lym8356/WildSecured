package com.fit5046.wildsecured;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SavedPlace {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "placeName")
    public String placeName;
    @ColumnInfo(name = "placeId")
    public String placeId;
    @ColumnInfo(name = "placeAddress")
    public String placeAddress;
    @ColumnInfo(name = "placeTotalRating")
    public int placeTotalRating;
    @ColumnInfo(name = "placeRating")
    public Double placeRating;
    @ColumnInfo(name = "placeLat")
    public Double placeLat;
    @ColumnInfo(name = "placeLon")
    public Double placeLon;
    @ColumnInfo(name = "isSelected")
    public boolean isSelected;

    public SavedPlace(String placeName, String placeAddress, String placeId, Double placeRating, int placeTotalRating, Double placeLat, Double placeLon) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeId = placeId;
        this.placeRating = placeRating;
        this.placeTotalRating = placeTotalRating;
        this.placeLat = placeLat;
        this.placeLon = placeLon;
        isSelected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public int getPlaceTotalRating() {
        return placeTotalRating;
    }

    public void setPlaceTotalRating(int placeTotalRating) {
        this.placeTotalRating = placeTotalRating;
    }

    public Double getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(Double placeRating) {
        this.placeRating = placeRating;
    }

    public Double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(Double placeLat) {
        this.placeLat = placeLat;
    }

    public Double getPlaceLon() {
        return placeLon;
    }

    public void setPlaceLon(Double placeLon) {
        this.placeLon = placeLon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
