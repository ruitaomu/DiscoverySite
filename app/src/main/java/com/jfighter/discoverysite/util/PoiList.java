package com.jfighter.discoverysite.util;

import android.location.Location;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class PoiList {
    private static Dictionary<String, PoiInfo> mPOIs = null;
    private static ArrayList<String> mPOIIndexes = null;

    private static int mTotalItems = 0;

    protected PoiList() {
        mTotalItems = 0;
        mPOIs = new Hashtable<>();
        mPOIIndexes = new ArrayList<>();
    }

    public PoiInfo getAtIndex(int index) {
        String name = mPOIIndexes.get(index);
        return getPOIByName(name);
    }

    protected int addPOI(String name, PoiInfo poiInfo) {
        int index = mTotalItems;
        mPOIs.put(name, poiInfo);
        mPOIIndexes.add(index, name);
        mTotalItems++;
        return index;
    }

    public PoiInfo getPOIByName(String name) {
        return mPOIs.get(name);
    }

    public Enumeration<String> getNames() {
        return mPOIs.keys();
    }

    public PoiInfo getPOIByLocation(Location location) {
        Enumeration<PoiInfo> pois = mPOIs.elements();
        while (pois.hasMoreElements()) {
            PoiInfo poi = pois.nextElement();
            Coordinate loc = poi.getmCoordinate();
            if (loc.X() == location.getLongitude() && loc.Y() == location.getLatitude()) {
                return poi;
            }
        }
        return null;
    }
}
