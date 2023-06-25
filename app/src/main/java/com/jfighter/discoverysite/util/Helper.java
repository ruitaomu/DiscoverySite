package com.jfighter.discoverysite.util;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.jfighter.discoverysite.R;
import com.jfighter.discoverysite.database.DiscoveryItemRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class Helper {

    private final static String TAG = "Helper";

    private static PoiList mPOIs = null;

    private static DiscoveryItemRepository mRepository;
    private static PoiList mHomeLocations = null;
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;

    public static SensorEventListener registerRotationUpdateListener(Fragment fragment, Context context, SensorEventListener listener) {
        ActivityResultLauncher<String> requestPermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        // 注册 LocationListener
        if (context == null) {
            return null;
        }

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);


        if (mSensorManager == null) {
            return null;
        }

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(listener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        return listener;
    }

    public static void removeRotationUpdater(Context context, SensorEventListener listener) {
        if (context != null && listener != null && mSensorManager != null){
            mSensorManager.unregisterListener(listener);
        }
    }

    public static LocationListener registerLocationUpdateListener(Fragment fragment, Context context, LocationListener listener) {
        ActivityResultLauncher<String> requestPermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        // 注册 LocationListener
        if (context == null) {
            return null;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return null;
        }

        boolean needApprove = true;
        while (needApprove) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
                needApprove = false;
            } catch (SecurityException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return listener;
    }

    public static void removeLocationUpdater(Context context, LocationListener listener) {
        if (context != null && listener != null){
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.removeUpdates(listener);
            }
        }
    }

    public static synchronized PoiList POI(Context context) {
        if (mPOIs == null && context != null) {
            JSONObject json = readPOIJSON(context, R.raw.poi);
            mPOIs = new PoiList();
            initPOIs(mPOIs, json);
        }
        return mPOIs;
    }

    public static synchronized PoiList POI() {
        return POI(null);
    }

    public static synchronized PoiList HomeLocation(Context context) {
        if (mHomeLocations == null && context != null) {
            JSONObject json = readPOIJSON(context, R.raw.home);
            mHomeLocations = new PoiList();
            initHomeLocations(mHomeLocations, json);
        }
        return mHomeLocations;
    }

    public static synchronized PoiList HomeLocation() { return HomeLocation(null); }

    public static synchronized DiscoveryItemRepository getDiscoveryItemRepository(Application application) {
        if (mRepository == null) {
            mRepository = new DiscoveryItemRepository(application);
        }
        return mRepository;
    }

    private static JSONObject readPOIJSON(Context context, int id) {
        String jsonString;
        JSONObject jsonObject = null;
        try {
            // 从资源文件中获取JSON文本数据
            InputStream inputStream = context.getResources().openRawResource(id);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");

            // 将JSON字符串转换为JSONObject对象
            jsonObject = new JSONObject(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    private static void initPOIs(PoiList poiList, JSONObject jsonObject) {
        try {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String name = keys.next();
                JSONObject item = jsonObject.getJSONObject(name);
                JSONArray coordinates = item.getJSONArray("coordinates");
                double lat = coordinates.getDouble(0);
                double lng = coordinates.getDouble(1);
                String filename = item.getString("imgname");
                String type = item.getString("type");
                String desc = item.getString("description");
                Log.d(TAG, "Name: " + name);
                Log.d(TAG, "Coordinates: " + lat + ", " + lng);
                Log.d(TAG, "Filename: " + filename);
                Log.d(TAG, "Type: " + type);
                poiList.addPOI(name, new PoiInfo(new Coordinate(lat, lng),
                                                    filename,
                                                    name, type, desc));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void initHomeLocations(PoiList poiList, JSONObject jsonObject) {
        try {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String name = keys.next();
                JSONObject item = jsonObject.getJSONObject(name);
                JSONArray coordinates = item.getJSONArray("coordinates");
                double lat = coordinates.getDouble(0);
                double lng = coordinates.getDouble(1);
                Log.d(TAG, "Name: " + name);
                Log.d(TAG, "Coordinates: " + lat + ", " + lng);
                poiList.addPOI(name, new PoiInfo(new Coordinate(lat, lng),
                        "",
                        name, "", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
