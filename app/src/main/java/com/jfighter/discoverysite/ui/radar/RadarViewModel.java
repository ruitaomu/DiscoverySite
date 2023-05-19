package com.jfighter.discoverysite.ui.radar;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.jfighter.discoverysite.util.Coordinate;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class RadarViewModel extends ViewModel implements  LocationListener {
    private Dictionary<String, Coordinate> mPOIs = new Hashtable<String, Coordinate>();

    private final MutableLiveData<String> mText;

    private ArrayList<Location> targetLocations;

    public RadarViewModel() {
        initPOIs();
        mText = new MutableLiveData<>();
        mText.setValue("No distance data");

        targetLocations = new ArrayList<>();

        Enumeration<String> keys = mPOIs.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Coordinate loc = mPOIs.get(key);
            Location targetLocation = new Location("");
            targetLocation.setLatitude(loc.Y());
            targetLocation.setLongitude(loc.X());
            targetLocations.add(targetLocation);
        }
    }

    private void initPOIs() {
        mPOIs.put("Athens Repository", new Coordinate(31.2304f, 121.4737f));
        mPOIs.put("Delphi Temple", new Coordinate(31.2304f, 121.4742f));
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float minDistance = Float.MAX_VALUE;
        for (Location loc: targetLocations) {
            float distance = location.distanceTo(loc);
            minDistance = Math.min(distance, minDistance);
        }
        // 计算距离
        mText.setValue(Float.toString(minDistance));
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}