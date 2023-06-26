package com.jfighter.discoverysite.ui.home;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jfighter.discoverysite.util.Helper;
import com.jfighter.discoverysite.util.PoiInfo;
import com.jfighter.discoverysite.util.PoiList;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements  LocationListener, SensorEventListener {

    private final MutableLiveData<String> mText;

    private ArrayList<Location> mHomeLocations;
    private final MutableLiveData<Float>  mBearing;
    private float mAzimuth = 0.0f;
    private float mDirection = 0.0f;

    public HomeViewModel() {
        mBearing = new MutableLiveData<>();
        mBearing.setValue((float)0.0);
        mText = new MutableLiveData<>();
        mText.setValue("正在定位...");
        mHomeLocations = new ArrayList<>();
        PoiList homePOIs = Helper.HomeLocation();
        int i = 0;
        PoiInfo home = null;
        while ((home = homePOIs.getAtIndex(i)) != null) {
            Location targetLocation = new Location("");
            targetLocation.setLatitude(home.getmCoordinate().getLatitude());
            targetLocation.setLongitude(home.getmCoordinate().getLongitude());
            mHomeLocations.add(targetLocation);
            i++;
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Float> getBearing() { return mBearing; }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float minDistance = Float.MAX_VALUE;
        Location nearestLocation = null;
        for (Location loc : mHomeLocations) {
            float distance = location.distanceTo(loc);
            if (distance < minDistance) {
                minDistance = distance;
                nearestLocation = loc;
            }
        }
        if (nearestLocation != null) {
            // 计算方向角度
            mDirection = location.bearingTo(nearestLocation);
            mBearing.setValue(mDirection - mAzimuth);
            // 计算距离
            mText.setValue(Integer.toString((int)location.distanceTo(nearestLocation)));
            //String pos = Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude());
            //mText.setValue(pos);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);
            mAzimuth = (float) Math.toDegrees(orientation[0]);
            mBearing.setValue(mDirection - mAzimuth);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}