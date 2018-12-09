package com.example.alexandroforte.citydata;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class displayAll extends AsyncTask<Void,Void,List<CityRecord>> {
    private AppDatabase db;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public Context context;
    double mag;
    List<CityRecord> items;

    public displayAll(AppDatabase db, RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context ) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.context = context;
    }


    @Override
    protected List<CityRecord> doInBackground(Void... m) {

        items = db.databaseInterface().getAllItems();
        System.out.println("in displayAll item size: " + items.size() );
        // Return the raw response to the onPostExecute
        //BUT remember, items is a local Arraylist for this thread
        return items;
    }

    @Override
    protected void onPostExecute(List<CityRecord> s) {
        super.onPostExecute(s);
        adapter= new RecyclerViewAdapter(items);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        MainActivity.items = items;
    }
}
