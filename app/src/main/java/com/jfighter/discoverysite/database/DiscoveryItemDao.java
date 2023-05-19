package com.jfighter.discoverysite.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiscoveryItemDao {

    // allowing the insert of the same discoveryItem multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DiscoveryItem discoveryItem);

    @Query("DELETE FROM discoveryitems_table")
    void deleteAll();

    @Query("SELECT * FROM discoveryitems_table ORDER BY name ASC")
    LiveData<List<DiscoveryItem>> getAlphabetizedNames();
}
