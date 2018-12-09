package com.example.alexandroforte.citydata;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class updateCity extends AsyncTask<String,Void,CityRecord> {
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;
    public Context context;
    public CityRecord city;

    // Constructor providing a reference to the views in MainActivity
    public updateCity(RecyclerView recyclerView, RecyclerViewAdapter adapter, Context context, CityRecord city) {
        this.db = Room.databaseBuilder(context, AppDatabase.class, db.NAME).fallbackToDestructiveMigration().build();
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
        this.city = city;
    }

    @Override
    protected CityRecord doInBackground(String... params) {
        db.databaseInterface().update(city);
        return city;
    }

    @Override
    protected void onPostExecute(CityRecord s) {
        super.onPostExecute(s);
        MainActivity.adapter = adapter;
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        db.close();
    }
}