package com.huoli.openapi.vo.data;

import java.util.List;

import com.huoli.openapi.util.Geohash;



public class GpsQuery {
    private Double lat;
    private Double lnt;
    private Double radius;
    private String geohash;
    private String hotelType;
    private List<String> geohashExt;
    private int limit = 8;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLnt() {
        return lnt;
    }

    public void setLnt(Double lnt) {
        this.lnt = lnt;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public List<String> getGeohashExt() {
        if (lat > 0 && lnt > 0) {
			Geohash geoh = new Geohash();
            geoh.setNumbits(10);
            geohashExt = geoh.expand(lat, lnt);
        }
        return geohashExt;
    }

    public void setGeohashExt(List<String> geohashExt) {
        this.geohashExt = geohashExt;
    }

}
