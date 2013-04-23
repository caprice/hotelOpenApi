package com.huoli.openapi.util;

import java.util.ArrayList;
import java.util.List;

import com.hrs.geo.GeoHash;

public class Geohash {

    private Integer numbits = 18;

    public Geohash(Integer numbits) {
        this.numbits = numbits;
    }

    public Geohash() {
    }

    public List<String> expand(String geohash) {
        GeoHash gh = GeoHash.fromGeohashString(geohash);
        GeoHash[] extend = gh.getAdjacent();
        List<String> ret = new ArrayList<String>();
        ret.add(geohash);
        for (GeoHash geoHash2 : extend) {
            ret.add(geoHash2.toBase32());
        }
        return ret;
    }

    public List<String> expand(double lat, double lon) {
        GeoHash gh = GeoHash.withCharacterPrecision(lat, lon, getNumberOfCharacters());
        GeoHash[] extend = gh.getAdjacent();
        List<String> ret = new ArrayList<String>();
        ret.add(gh.toBase32());
        for (GeoHash geoHash2 : extend) {
            ret.add(geoHash2.toBase32());
        }
        return ret;
    }

    private int getNumberOfCharacters() {
        return numbits * 2 / 5;
    }

    public String encode(double lat, double lon) {
        GeoHash gh = GeoHash.withCharacterPrecision(lat, lon, getNumberOfCharacters());
        return gh.toBase32();
    }

    public int getNumbits() {
        return numbits;
    }

    public void setNumbits(int numbits) {
        this.numbits = numbits;
    }

    public static void main(String[] args) {
        // double[] latlon = new Geohash().decode("dj248j248j24");
        // System.out.println(latlon[0] + " " + latlon[1]);

        Geohash e = new Geohash();
        String s = e.encode(30.062289237027624D, 118.1822755145921D);
        System.out.println(s);

        List<String> ret = e.expand(s);
        for (String str : ret) {
            System.out.println(str);
        }

        // latlon = e.decode(s);
        // System.out.println(latlon[0] + ", " + latlon[1]);
    }
}
