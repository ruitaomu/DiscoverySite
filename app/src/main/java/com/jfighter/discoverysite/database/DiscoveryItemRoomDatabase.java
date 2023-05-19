package com.jfighter.discoverysite.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DiscoveryItem.class}, version = 1, exportSchema = false)
public abstract class DiscoveryItemRoomDatabase extends RoomDatabase {

    public abstract DiscoveryItemDao discoveryItemDao();

    private static volatile DiscoveryItemRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DiscoveryItemRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DiscoveryItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DiscoveryItemRoomDatabase.class, "discoveryitems_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                DiscoveryItemDao dao = INSTANCE.discoveryItemDao();
                dao.deleteAll();

                DiscoveryItem discoveryItem = new DiscoveryItem("Hello");
                dao.insert(discoveryItem);
                discoveryItem = new DiscoveryItem("Athens Repository");
                dao.insert(discoveryItem);
                discoveryItem = new DiscoveryItem("Delphi Temple");
                dao.insert(discoveryItem);
            });
        }
    };
}
