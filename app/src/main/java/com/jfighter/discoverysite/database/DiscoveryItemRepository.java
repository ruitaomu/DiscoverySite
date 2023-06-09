package com.jfighter.discoverysite.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryItemRepository {

    private DiscoveryItemDao mDiscoveryItemDao;
    private LiveData<List<DiscoveryItem>> mAllNamesLiveData;
    private volatile List<DiscoveryItem> mAllNames;

    // Note that in order to unit test the DiscoveryItemRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public DiscoveryItemRepository(Application application) {
        DiscoveryItemRoomDatabase db = DiscoveryItemRoomDatabase.getDatabase(application);
        mDiscoveryItemDao = db.discoveryItemDao();
        mAllNamesLiveData = mDiscoveryItemDao.getAlphabetizedNames();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<DiscoveryItem>> getAllNames() {
        return mAllNamesLiveData;
    }

    public List<String> retrieveAllNames() {
        mAllNames = null;
        DiscoveryItemRoomDatabase.databaseWriteExecutor.execute(() -> {
            mAllNames = mDiscoveryItemDao.retrieveAlphabetizedNames();
        });

        while (mAllNames == null);

        List<String> names = new ArrayList<String>();
        for (DiscoveryItem item : mAllNames) {
            names.add(item.getName());
        }
        return names;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(DiscoveryItem discoveryItem) {
        DiscoveryItemRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDiscoveryItemDao.insert(discoveryItem);
        });
    }
}
