package com.example.alexandroforte.citydata;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CityRecord implements Serializable {

    @PrimaryKey(autoGenerate=true)
    public int id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public int geonameId;

    @ColumnInfo
    public double lon;

    @ColumnInfo
    public double lat;

    @ColumnInfo
    public String wiki;

    @ColumnInfo
    public double userScore;

    @ColumnInfo
    public double dist;

    public CityRecord() {
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeonameId() {
        return geonameId;
    }

    public void setGeonameId(int geonameId) {
        this.geonameId = geonameId;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }
}
