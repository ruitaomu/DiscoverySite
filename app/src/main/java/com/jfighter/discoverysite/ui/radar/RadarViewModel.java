package com.jfighter.discoverysite.ui.radar;

import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jfighter.discoverysite.database.DiscoveryItemRepository;
import com.jfighter.discoverysite.util.Coordinate;
import com.jfighter.discoverysite.util.PoiInfo;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class RadarViewModel extends AndroidViewModel implements  LocationListener {
    private Dictionary<String, PoiInfo> mPOIs = new Hashtable<String, PoiInfo>();

    private final MutableLiveData<String> mText;

    private ArrayList<Location> targetLocations;

    public RadarViewModel(Application application) {
        super(application);

        initPOIs();
        mText = new MutableLiveData<>();
        mText.setValue("No distance data");

        targetLocations = new ArrayList<>();

        List<String> discoveredNames = new DiscoveryItemRepository(application).retrieveAllNames();

        Enumeration<String> keys = mPOIs.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (!discoveredNames.contains(key)) {
                Coordinate loc = mPOIs.get(key).getmCoordinate();
                Location targetLocation = new Location("");
                targetLocation.setLatitude(loc.Y());
                targetLocation.setLongitude(loc.X());
                targetLocations.add(targetLocation);
            }
        }
    }

    private void initPOIs() {
        mPOIs.put("Athens Repository", new PoiInfo(
                    new Coordinate(31.2304f, 121.4737f),
                    "1",
                    "The great repository of Athens",
                    "arch"));
        mPOIs.put("Delphi Temple", new PoiInfo(
                    new Coordinate(31.2304f, 121.4742f),
                    "2",
                    "Apollo's main temple in Delphi",
                    "arch"));
        mPOIs.put("Apollo Statue", new PoiInfo(
                new Coordinate(31.2306f, 121.4742f),
                "3",
                "Apollo's great statue",
                "Statue"));
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

        if (minDistance != Float.MAX_VALUE) {
            mText.setValue(Float.toString(minDistance));
        }
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