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

    public SavedPlace(String placeName, String placeAddress, String placeId, Double placeRating, int placeTotalRating, Double placeLat, Double placeLon) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeId = placeId;
        this.placeRating = placeRating;
        this.placeTotalRating = placeTotalRating;
        this.placeLat = placeLat;
        this.placeLon = placeLon;
    }
}
