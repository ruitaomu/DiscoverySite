package com.jfighter.discoverysite.ui.radar;

import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jfighter.discoverysite.database.DiscoveryItem;
import com.jfighter.discoverysite.database.DiscoveryItemRepository;
import com.jfighter.discoverysite.util.Coordinate;
import com.jfighter.discoverysite.util.Helper;
import com.jfighter.discoverysite.util.PoiInfo;
import com.jfighter.discoverysite.util.PoiList;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RadarViewModel extends AndroidViewModel implements  LocationListener {

    private final static String TAG = "RadarViewModel";
    private final MutableLiveData<String> mText;
    DiscoveryItemRepository mDiscoveredItemRepository;
    private final ArrayList<Location> mTargetLocations;

    public RadarViewModel(Application application) {
        super(application);

        PoiList pois = Helper.POI();
        mText = new MutableLiveData<>();
        mText.setValue("No distance data");

        mTargetLocations = new ArrayList<>();

        mDiscoveredItemRepository = Helper.getDiscoveryItemRepository(application);
        List<String> discoveredNames = mDiscoveredItemRepository.retrieveAllNames();

        Enumeration<String> names = pois.getNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!discoveredNames.contains(name)) {
                Coordinate loc = pois.getPOIByName(name).getmCoordinate();
                Location targetLocation = new Location("");
                targetLocation.setLatitude(loc.Y());
                targetLocation.setLongitude(loc.X());
                mTargetLocations.add(targetLocation);
            }
        }

    }


    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float minDistance = Float.MAX_VALUE;
        Location nearestTargetLocation = null;
        for (Location loc: mTargetLocations) {
            float distance = location.distanceTo(loc);
            minDistance = Math.min(distance, minDistance);
            if (distance == minDistance) {
                nearestTargetLocation = loc;
            }
        }
        // 计算距离

        if (minDistance != Float.MAX_VALUE && nearestTargetLocation != null) {
            if (minDistance < 10.0) {
                mTargetLocations.remove(nearestTargetLocation);
                Log.d(TAG, "Discovered a site!");
                PoiInfo poi = Helper.POI().getPOIByLocation(nearestTargetLocation);
                if (poi != null) {
                    mText.setValue(poi.getmSiteName());
                    mDiscoveredItemRepository.insert(new DiscoveryItem(poi.getmSiteName()));
                } else {
                    Log.e(TAG, "Discovered site location is missing in target list");
                }
            } else {
                mText.setValue(Float.toString(minDistance));
            }
        } else {
            mText.setValue("No distance data");
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