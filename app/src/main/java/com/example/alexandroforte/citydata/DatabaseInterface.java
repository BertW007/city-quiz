package com.example.alexandroforte.citydata;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DatabaseInterface {

    @Query("SELECT * FROM CityRecord")
    List<CityRecord> getAllItems();

    @Insert
    void insertAll(CityRecord... cityRecords);

    @Update(onConflict = REPLACE)
    public void update(CityRecord city);

    @Query("DELETE FROM CityRecord")
    public void dropTheTable();
}