package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by Christoph on 29.09.2014.
 */
public class ActiveChallengesAdapter extends ArrayAdapter<Challenge> {
    private final Context context;
    private final List<Challenge> values;

    public ActiveChallengesAdapter(Context context, List<Challenge> challenges) {
        super(context, R.layout.active_challenge_item, challenges);
        this.context = context;
        this.values = challenges;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.active_challenge_item, parent, false);

        TextView textViewActivityName = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textViewActivityLastTime = (TextView) rowView.findViewById(R.id.thirdLine);
        ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textViewActivityName.setText(values.get(position).getTitle());
        Date today = new Date();
        long lastTime = values.get(position).getTimestampLastModified();
        double timeDifference = (today.getTime() - lastTime)/3600.0/1000/24.0;
        textViewActivityLastTime.setText("Last activity " + (int) Math.round(timeDifference) + " days ago.");
        if(timeDifference<7){
            textViewActivityLastTime.setTextColor(Color.parseColor("#00FFFF"));
        } else if(timeDifference >=7 && timeDifference < 13){
            textViewActivityLastTime.setTextColor(Color.parseColor("#AAAAFF"));
        } else {
            textViewActivityLastTime.setTextColor(Color.parseColor("#FF0000"));
        }
        progressBar.setProgress((int) (values.get(position).getCompletedEffort()/values.get(position).getTotalEffort()));
        Drawable iconChallenge = Drawable.createFromPath(values.get(position).getIconPath());
        imageView.setImageDrawable(iconChallenge);
        return rowView;
    }
}