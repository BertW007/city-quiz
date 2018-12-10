package com.example.alexandroforte.citydata;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

// bump version number if your schema changes
@Database(entities = {CityRecord.class},version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DatabaseInterface databaseInterface();

    // Database name to be used
    public static final String NAME = "MyCities";

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}