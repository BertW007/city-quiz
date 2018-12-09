package com.example.alexandroforte.citydata;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private LatLng latLng;
    private double lat1, lon1;
    private CityRecord city;
    Geocoder geocoder = null;
    ArrayList<String> locations; //will contain all the locations

    TextView welcomeText;
    Button submitBtn;
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        bar.setTitle(Html.fromHtml("<font color=\"white\"> Map </font>"));
        bar.setDisplayHomeAsUpEnabled(true);  //helps with returning to our MainActivity
        welcomeText = findViewById(R.id.mapWelcome);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setEnabled(false);

        geocoder = new Geocoder(this);
        locations = new ArrayList<String>();

        // This fetches the addresses from a bundle and places them in an ArrayList
        // ArrayList will be used later by GeoCoder
        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();

        city = (CityRecord) bundle.getSerializable("city");

        welcomeText.setText("TRY TO FIND " + city.getName().toUpperCase() + "!");

        lat1 = city.getLat();
        lon1 = city.getLon();

        //gets the maps to load
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAnswer(marker);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this will catch the <- arrow
        //and return to MainActivity
        //needed since we use fragments to map sites
        switch (item.getItemId()) {
            case android.R.id.home:
                goBackToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setOnMapLoadedCallback(this);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
    }


    @Override
    public void onMapLoaded() {
        getMoreInfo();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
    }

    public void goBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void submitAnswer(Marker marker) {
        LatLng position = marker.getPosition();

        double guessedLat = position.latitude;
        double guessedLon = position.longitude;
        double correctLat = lat1;
        double correctLon = lon1;

        Location guessedLocation = new Location("");
        guessedLocation.setLatitude(guessedLat);
        guessedLocation.setLongitude(guessedLon);

        Location correctLocation = new Location("");
        correctLocation.setLatitude(correctLat);
        correctLocation.setLongitude(correctLon);

        double distanceInMeters = guessedLocation.distanceTo(correctLocation);

        double score;

        if (distanceInMeters < 100000) {
            score = 10;
        } else if (distanceInMeters < 200000) {
            score = 9;
        } else if (distanceInMeters < 300000) {
            score = 8;
        } else if (distanceInMeters < 400000) {
            score = 7;
        } else if (distanceInMeters < 500000) {
            score = 6;
        } else if (distanceInMeters < 600000) {
            score = 5;
        } else if (distanceInMeters < 700000) {
            score = 4;
        } else if (distanceInMeters < 800000) {
            score = 3;
        } else if (distanceInMeters < 900000) {
            score = 2;
        } else {
            score = 1;
        }

        Toast.makeText(this, "You received a " + (int) score + " for " + city.getName(), Toast.LENGTH_LONG).show();

        city.setUserScore(score);
        city.setDist(distanceInMeters);

        MainActivity.load(city);

        goBackToMain();
    }

    public void getMoreInfo() {
        // This sets the marker to start in the middle of the ocean every time
        double lat = 34.832295;
        double lon = -41.073509;

        latLng = new LatLng(lat, lon);
        map.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {}
        });
        Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        this.marker = marker;
        submitBtn.setEnabled(true);
    }
}
