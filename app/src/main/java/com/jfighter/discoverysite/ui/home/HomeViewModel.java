package com.jfighter.discoverysite.ui.home;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel implements  LocationListener {

    private final MutableLiveData<String> mText;

    private Location targetLocation;
    private final MutableLiveData<Float>  mBearing;

    public HomeViewModel() {
        mBearing = new MutableLiveData<>();
        mBearing.setValue((float)0.0);
        mText = new MutableLiveData<>();
        mText.setValue("Looking for GPS signal...");
        targetLocation = new Location("");
        targetLocation.setLatitude(31.2304);
        targetLocation.setLongitude(121.4737);

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Float> getBearing() { return mBearing; }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (targetLocation != null) {
            // 计算方向角度
            mBearing.setValue(location.bearingTo(targetLocation));
            // 计算距离
            mText.setValue(Float.toString(location.distanceTo(targetLocation)));
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