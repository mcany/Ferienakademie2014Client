package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import de.ferienakademie.neverrest.R;

/**
 * Created by Christoph on 29.09.2014.
 */
public class ActiveChallengesAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final Drawable[] icons;
    private final int[] progress;
    private final Date[] date;

    public ActiveChallengesAdapter(Context context, String[] challenges, Drawable[]icons, int[] progress, Date[]date) {
        super(context, R.layout.active_challenge_item, challenges);
        this.context = context;
        this.values = challenges;
        this.icons = icons;
        this.progress = progress;
        this.date = date;
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

        textViewActivityName.setText(values[position]);
        Date today = new Date();
        Date lastTime = date[position];
        double timeDifference = (today.getTime() - lastTime.getTime())/3600.0/24.0;

        textViewActivityLastTime.setText((int) Math.round(timeDifference));
        progressBar.setProgress(progress[position]);
        imageView.setImageDrawable(icons[position]);

        return rowView;
    }
}