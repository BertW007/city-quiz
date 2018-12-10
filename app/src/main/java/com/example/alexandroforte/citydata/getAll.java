package com.example.alexandroforte.citydata;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class getAll extends AsyncTask<String,Void,List<CityRecord>> {
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;
    public Context context;  //done as a hack to get app context
    List<CityRecord> items;
    int index;
    boolean restart;

    public getAll(RecyclerView recyclerView, RecyclerViewAdapter adapter, Context context, boolean restart) {
        this.db = Room.databaseBuilder(context, AppDatabase.class, db.NAME).fallbackToDestructiveMigration().build();
        this.restart = restart;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected List<CityRecord> doInBackground(String... params) {
        items = db.databaseInterface().getAllItems();
        return items;
    }

    @Override
    protected void onPostExecute(List<CityRecord> s) {
        super.onPostExecute(s);
        adapter = new RecyclerViewAdapter(items);
        MainActivity.adapter = adapter;
        recyclerView.setAdapter(adapter);

        if (items == null || items.size() == 0 || restart) {
            new MainActivity.HttpGetTask().execute("http://api.geonames.org/citiesJSON?north=90.0&south=0.0&east=-50&west=0.0&lang=de&maxRows=200&username=forte");
        } else {
            MainActivity.items = items;
        }

        db.close();
    }
}