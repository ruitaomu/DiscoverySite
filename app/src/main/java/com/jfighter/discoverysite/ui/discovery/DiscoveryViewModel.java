package com.jfighter.discoverysite.ui.discovery;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jfighter.discoverysite.database.DiscoveryItem;
import com.jfighter.discoverysite.database.DiscoveryItemRepository;

import java.util.List;

public class DiscoveryViewModel extends AndroidViewModel {

    private DiscoveryItemRepository mRepository;

    private final LiveData<List<DiscoveryItem>> mAllNames;

    public DiscoveryViewModel(Application application) {
        super(application);
        mRepository = new DiscoveryItemRepository(application);
        mAllNames = mRepository.getAllNames();
    }

    LiveData<List<DiscoveryItem>> getAllNames() { return mAllNames; }

    public void insert(DiscoveryItem discoveryItem) { mRepository.insert(discoveryItem); }
}

