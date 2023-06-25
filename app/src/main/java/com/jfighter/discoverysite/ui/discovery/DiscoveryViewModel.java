package com.jfighter.discoverysite.ui.discovery;


import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jfighter.discoverysite.database.DiscoveryItem;
import com.jfighter.discoverysite.database.DiscoveryItemRepository;
import com.jfighter.discoverysite.util.Coordinate;
import com.jfighter.discoverysite.util.Helper;
import com.jfighter.discoverysite.util.PoiInfo;
import com.jfighter.discoverysite.util.PoiList;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DiscoveryViewModel extends AndroidViewModel  implements LocationListener {

    private static final String TAG = "DiscoveryViewModel";
    private final DiscoveryItemRepository mDiscoveredItemRepository;

    private final LiveData<List<DiscoveryItem>> mAllNames;

    private final ArrayList<Location> mTargetLocations;

    public DiscoveryViewModel(Application application) {
        super(application);

        mTargetLocations = new ArrayList<>();

        mDiscoveredItemRepository = Helper.getDiscoveryItemRepository(application);
        mAllNames = mDiscoveredItemRepository.getAllNames();

        List<String> discoveredNames = mDiscoveredItemRepository.retrieveAllNames();
        PoiList pois = Helper.POI();
        Enumeration<String> names = pois.getNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!discoveredNames.contains(name)) {
                Coordinate loc = pois.getPOIByName(name).getmCoordinate();
                Location targetLocation = new Location("");
                targetLocation.setLatitude(loc.getLatitude());
                targetLocation.setLongitude(loc.getLongitude());
                mTargetLocations.add(targetLocation);
            }
        }
    }

    LiveData<List<DiscoveryItem>> getAllNames() { return mAllNames; }

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
                    mDiscoveredItemRepository.insert(new DiscoveryItem(poi.getmSiteName()));
                } else {
                    Log.e(TAG, "Discovered site location is missing in target list");
                }
            }
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
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}

