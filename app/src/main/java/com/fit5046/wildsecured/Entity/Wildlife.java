package com.fit5046.wildsecured.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity
public class Wildlife {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "commonName")
    private String commonName;
    @ColumnInfo(name = "scientificName")
    private String scientificName;
    @ColumnInfo(name = "briefDescription")
    private String briefDescription;
    @ColumnInfo(name = "identification")
    private String identification;
    @ColumnInfo(name = "habitat")
    private String habitat;
    @ColumnInfo(name = "riskToHuman")
    private String riskToHuman;
    @ColumnInfo(name = "dangerLevel")
    private String dangerLevel;
    @ColumnInfo(name = "wildlifeGroup")
    private String wildlifeGroup;
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    public Wildlife(String commonName, String scientificName, String briefDescription, String identification,
                    String habitat, String riskToHuman,  String dangerLevel, String wildlifeGroup, String imageUrl) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.briefDescription = briefDescription;
        this.identification = identification;
        this.habitat = habitat;
        this.riskToHuman = riskToHuman;
        this.dangerLevel = dangerLevel;
        this.wildlifeGroup = wildlifeGroup;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRiskToHuman() {
        return riskToHuman;
    }

    public void setRiskToHuman(String riskToHuman) {
        this.riskToHuman = riskToHuman;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getWildlifeGroup() {
        return wildlifeGroup;
    }

    public void setWildlifeGroup(String wildlifeGroup) {
        this.wildlifeGroup = wildlifeGroup;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Comparator<Wildlife> sortByNameAsc = new Comparator<Wildlife>() {
        @Override
        public int compare(Wildlife o1, Wildlife o2) {
            return o1.getCommonName().compareTo(o2.getCommonName());
        }
    };

    public static Comparator<Wildlife> sortByNameDesc = new Comparator<Wildlife>() {
        @Override
        public int compare(Wildlife o1, Wildlife o2) {
            return o2.getCommonName().compareTo(o1.getCommonName());
        }
    };

    public static Comparator<Wildlife> sortByDangerLevelAsc = new Comparator<Wildlife>() {
        @Override
        public int compare(Wildlife o1, Wildlife o2) {
            return Integer.parseInt(o1.getDangerLevel()) - Integer.parseInt(o2.getDangerLevel());
        }
    };

    public static Comparator<Wildlife> sortByDangerLevelDesc = new Comparator<Wildlife>() {
        @Override
        public int compare(Wildlife o1, Wildlife o2) {
            return Integer.parseInt(o2.getDangerLevel()) - Integer.parseInt(o1.getDangerLevel());
        }
    };
}
