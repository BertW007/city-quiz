package com.example.alexandroforte.citydata;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<CityRecord> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    AppDatabase db;
    CityRecord fred;
    TextToSpeech tts;

    public RecyclerViewAdapter(List<CityRecord> items) {
        this.mData = items;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row,parent,false);
        mContext = parent.getContext();
        db = Room.databaseBuilder(mContext, AppDatabase.class,AppDatabase.NAME).build();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CityRecord city = mData.get(position);
        final CityRecord city1 = city;

        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://" + city1.wiki;
                Uri uri = Uri.parse(url);
                Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(myIntent);
            }
        });

        holder.name.setText(city.getName());

        double score = city.getUserScore();

        int yellow = Color.rgb(222, 215, 18);

        if (score == 10) {
            holder.score.setText("10/10");
            holder.name.setTextColor(Color.CYAN);
            holder.score.setTextColor(Color.CYAN);
        } else if (score == 9) {
            holder.score.setText(" 9/10");
            holder.name.setTextColor(Color.GREEN);
            holder.score.setTextColor(Color.GREEN);
        } else if (score == 8) {
            holder.score.setText(" 8/10");
            holder.name.setTextColor(Color.GREEN);
            holder.score.setTextColor(Color.GREEN);
        } else if (score == 7) {
            holder.score.setText(" 7/10");
            holder.name.setTextColor(Color.GREEN);
            holder.score.setTextColor(Color.GREEN);
        } else if (score == 6) {
            holder.score.setText(" 6/10");
            holder.name.setTextColor(yellow);
            holder.score.setTextColor(yellow);
        } else if (score == 5) {
            holder.score.setText(" 5/10");
            holder.name.setTextColor(yellow);
            holder.score.setTextColor(yellow);
        } else if (score == 4) {
            holder.score.setText(" 4/10");
            holder.name.setTextColor(yellow);
            holder.score.setTextColor(yellow);
        } else if (score == 3) {
            holder.score.setText(" 3/10");
            holder.name.setTextColor(Color.RED);
            holder.score.setTextColor(Color.RED);
        } else if (score == 2) {
            holder.score.setText(" 2/10");
            holder.name.setTextColor(Color.RED);
            holder.score.setTextColor(Color.RED);
        } else if (score == 1) {
            holder.score.setText(" 1/10");
            holder.name.setTextColor(Color.RED);
            holder.score.setTextColor(Color.RED);
        } else {
            holder.score.setText(" -/10");
            holder.name.setTextColor(Color.DKGRAY);
            holder.score.setTextColor(Color.DKGRAY);
        }

        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fred = mData.get(index);

                // Shouldn't do anything when clicking on a city with a score
                if (fred.getUserScore() > 0) {
                    return;
                }

                Intent map = new Intent(mContext, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("city", mData.get(index));
                map.putExtras(bundle);
                mContext.startActivity(map);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR){
                            tts.setLanguage(Locale.US);
                            tts.speak(city.getName(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public CityRecord getFirstItem() {
        return mData.get(0);
    }

    public List<CityRecord> getAllItems() {
        return mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView score;
        Button infoBtn;


        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            infoBtn = itemView.findViewById(R.id.infoBtn);
        }
    }
}
