package com.jfighter.discoverysite.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "discoveryitems_table")
public class DiscoveryItem {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    public DiscoveryItem(@NonNull String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }
}
