package com.example.alexandroforte.citydata;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    public static List<CityRecord> items;
    TextView bestLabel;
    TextView worstLabel;
    TextView totalLabel;
    TextView avgLabel;
    TextView numAnsLabel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
        items = MainActivity.items;

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        bar.setTitle(Html.fromHtml("<font color=\"white\"> Stats </font>"));
        bar.setDisplayHomeAsUpEnabled(true);  //helps with returning to our MainActivity

        bestLabel = findViewById(R.id.bestLabel);
        worstLabel = findViewById(R.id.worstLabel);
        totalLabel = findViewById(R.id.totalLabel);
        avgLabel = findViewById(R.id.avgLabel);
        numAnsLabel = findViewById(R.id.numAnsLabel);

        iterateItems(items);
    }

    private void iterateItems(List<CityRecord> items) {
        String bestCity = "";
        String worstCity = "";
        double bestGuess = Double.MAX_VALUE;
        double worstGuess = -1;
        int totalPotential = 0;
        int totalEarned = 0;
        int numAnswered = 0;
        for (CityRecord city : items) {
            double score = city.getUserScore();
            double dist = city.getDist();
            String name = city.getName();

            if (score != -1) {
                numAnswered++;
                totalPotential += 10;
                totalEarned += score;

                if (dist < bestGuess) {
                    bestCity = name;
                    bestGuess = dist;
                }

                if (dist > worstGuess) {
                    worstCity = name;
                    worstGuess = dist;
                }
            }
        }

        if (!bestCity.equals("")) {
            bestLabel.setText(bestCity);
        }

        if (!worstCity.equals("")) {
            worstLabel.setText(worstCity);
        }

        if (numAnswered > 0) {
            totalLabel.setText(Integer.toString(totalEarned) + " / " + Integer.toString(totalPotential));
            double avg = ((double) totalEarned /  (double) totalPotential)*10;
            avgLabel.setText(Double.toString(round(avg, 2)) + " / 10");
        }

        numAnsLabel.setText("You have completed " + Integer.toString(numAnswered) + " out of " + items.size() + " cities.");

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

    public void goBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
