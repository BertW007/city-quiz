package com.example.alexandroforte.citydata;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class loadDataBase extends AsyncTask<List<CityRecord>, Void, List<CityRecord>> {

    // Variables for the search input field, and results TextViews
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;
    public Context context;  //done as a hack to get app context
    List<CityRecord> items;

    // Constructor providing a reference to the views in MainActivity
    public loadDataBase(RecyclerView recyclerView, RecyclerViewAdapter adapter, Context context ) {
        this.db = Room.databaseBuilder(context, AppDatabase.class, db.NAME).fallbackToDestructiveMigration().build();
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<CityRecord> doInBackground(List<CityRecord>... params) {
        items = params[0];
        db.databaseInterface().dropTheTable(); //clear any data before reloading
        for (int i =0; i< items.size(); i++) {
            db.databaseInterface().insertAll(items.get(i));
        }
        System.out.println("item size: " + items.size() );
        return items;
    }

    @Override
    protected void onPostExecute(List<CityRecord> s) {
        super.onPostExecute(s);
        adapter = new RecyclerViewAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
//        for (CityRecord city2: items) {
//            city2.setUserScore(-2);
//            new updateCity(recyclerView, adapter, context, city2);
//        }
        db.close();
    }
}

