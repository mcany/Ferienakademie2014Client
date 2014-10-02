package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by arno on 29/09/14.
 */
public class HistoryListAdapter extends ArrayAdapter<Challenge> {

    private final Context context;
    private final ArrayList<Challenge> values;

    public HistoryListAdapter(Context context, ArrayList<Challenge> values) {
        super(context, de.ferienakademie.neverrest.R.layout.list_history, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(de.ferienakademie.neverrest.R.layout.list_history, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
		textView.setTextColor(context.getResources().getColor(android.R.color.black));
        TextView textViewDetails = (TextView) rowView.findViewById(R.id.secondLine);
		textViewDetails.setTextColor(context.getResources().getColor(android.R.color.black));
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values.get(position).getTitle());
        textViewDetails.setText(values.get(position).getDescription());

        // Show image if there is one
		/* TODO challenge.getIconResourceId should be uesd in challenge details screen, not as a small icon in list */
		/*
        if (values.get(position) != null && !values.get(position).getIconPath().isEmpty()) {
            imageView.setImageBitmap(values.get(position).getIconAsBitmap());
        }
        */
        return rowView;
    }

}
