package com.fit5046.wildsecured;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WildLifeDataModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("commonName")
    @Expose
    private String commonName;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("kingdom")
    @Expose
    private String kingdom;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("rank")
    @Expose
    private String rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
