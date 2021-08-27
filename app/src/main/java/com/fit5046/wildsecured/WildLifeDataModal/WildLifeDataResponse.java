package com.fit5046.wildsecured.WildLifeDataModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WildLifeDataResponse {
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("geom_idx")
    @Expose
    private Integer geomIdx;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("family_lsid")
    @Expose
    private String familyLsid;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("endemic")
    @Expose
    private Boolean endemic;
    @SerializedName("genus_name")
    @Expose
    private String genusName;
    @SerializedName("scientific")
    @Expose
    private String scientific;
    @SerializedName("specific_n")
    @Expose
    private String specificN;
    @SerializedName("genus_lsid")
    @Expose
    private String genusLsid;
    @SerializedName("lsid")
    @Expose
    private String lsid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("area_name")
    @Expose
    private String areaName;
    @SerializedName("data_resource_uid")
    @Expose
    private String dataResourceUid;
    @SerializedName("common_nam")
    @Expose
    private String commonNam;
    @SerializedName("spcode")
    @Expose
    private Integer spcode;
    @SerializedName("area_km")
    @Expose
    private Double areaKm;
    @SerializedName("gid")
    @Expose
    private Integer gid;
    @SerializedName("wmsurl")
    @Expose
    private String wmsurl;
    @SerializedName("authority_")
    @Expose
    private String authority;
    @SerializedName("metadata_u")
    @Expose
    private String metadataU;
    @SerializedName("caab_family_number")
    @Expose
    private String caabFamilyNumber;
    @SerializedName("caab_species_number")
    @Expose
    private String caabSpeciesNumber;

    public String getFamily() {
        return family;
    }

    public Integer getGeomIdx() {
        return geomIdx;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getFamilyLsid() {
        return familyLsid;
    }

    public String getPid() {
        return pid;
    }

    public Boolean getEndemic() {
        return endemic;
    }

    public String getGenusName() {
        return genusName;
    }

    public String getScientific() {
        return scientific;
    }

    public String getSpecificN() {
        return specificN;
    }

    public String getGenusLsid() {
        return genusLsid;
    }

    public String getLsid() {
        return lsid;
    }

    public String getType() {
        return type;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getDataResourceUid() {
        return dataResourceUid;
    }

    public String getCommonNam() {
        return commonNam;
    }

    public Integer getSpcode() {
        return spcode;
    }

    public Double getAreaKm() {
        return areaKm;
    }

    public Integer getGid() {
        return gid;
    }

    public String getWmsurl() {
        return wmsurl;
    }

    public String getAuthority() {
        return authority;
    }

    public String getMetadataU() {
        return metadataU;
    }

    public String getCaabFamilyNumber() {
        return caabFamilyNumber;
    }

    public String getCaabSpeciesNumber() {
        return caabSpeciesNumber;
    }
}
