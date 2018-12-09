package com.example.alexandroforte.citydata;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase db;
    public static List<CityRecord> items;
    public static RecyclerViewAdapter adapter;
    public static RecyclerView recyclerView;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        bar.setTitle(Html.fromHtml("<font color=\"white\"> City Quiz </font>"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.setAdapter(adapter);

        new getAll(recyclerView, adapter, context, false).execute(); // json fetching in this class
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public static void load(CityRecord city) {
        new updateCity(recyclerView, adapter, context, city).execute();
    }

    public void goToStats() {
        Intent myStatsIntent = new Intent(MainActivity.this, StatsActivity.class);
        startActivity(myStatsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // --- Switch on the item ID that was clicked
        switch (item.getItemId()) {
            case R.id.seeStats:
                goToStats();
                break;

            case R.id.startNew:
                new getAll(recyclerView, adapter, context, true).execute(); // json fetching in this class
                Toast.makeText(this, "Your game is being reset!", Toast.LENGTH_LONG).show();
                break;

            case R.id.sort:
                sortItems();
                break;

            default:
                System.out.println("Hit Default! Should not be here!!");
                break;
        }
        return true;
    }

    public void sortItems() {
        Collections.sort(items, new Comparator<CityRecord>(){
            public int compare(CityRecord o1, CityRecord o2){
                if (o1.userScore == o2.userScore)
                    return 0;
                return o1.userScore > o2.userScore ? -1 : 1;
            }
        });

        adapter.notifyDataSetChanged();
    }

    public static class HttpGetTask extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String queryString = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String bookJSONString = null;
            try {

                URL requestURL = new URL(queryString);

                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                    publishProgress();
                }

                if (builder.length() == 0) {
                    return null;
                }
                bookJSONString = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return bookJSONString;
        }

        @Override
        protected void onPostExecute(String s) {
            items = new ArrayList<CityRecord>();
            String toponymName;
            int geonameId;
            double lat;
            double lon;
            String wiki;

            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s);

                // Get the JSONArray of book items.
                JSONArray itemsArray = jsonObject.getJSONArray("geonames");

                int i = 0;

                while (i < itemsArray.length()) {
                    JSONObject cityObject = itemsArray.getJSONObject(i);
                    toponymName = cityObject.getString("toponymName");
                    geonameId = cityObject.getInt("geonameId");
                    lat = cityObject.getDouble("lat");
                    lon = cityObject.getDouble("lng");
                    wiki = cityObject.getString("wikipedia");
                    CityRecord city = new CityRecord();
                    city.setGeonameId(geonameId);
                    city.setName(toponymName);
                    city.setLat(lat);
                    city.setLon(lon);
                    city.setWiki(wiki);
                    city.setUserScore(-1.0);
                    city.setDist(-1.0);
                    items.add(city);
                    i++;
                }
                Collections.shuffle(items);
                new loadDataBase(recyclerView,  adapter, context).execute(items);
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
