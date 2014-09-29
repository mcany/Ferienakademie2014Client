package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.HistoryListAdapter;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by arno on 29/09/14.
 * adapted from http://www.vogella.com/tutorials/AndroidListView/article.html
 */
public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final ListView listview = (ListView) findViewById(R.id.historyList);

        Challenge c1 = new Challenge("iPhone", "Hier könnte Ihre Werbung stehen", R.drawable.sampleimage);
        Challenge c2 = new Challenge("Nexus123", "Die wo ich jetzt schon hab", R.drawable.ic_launcher);
        ArrayList<Challenge> list = new ArrayList<Challenge>();
        list.add(c1);
        list.add(c2);

        final HistoryListAdapter adapter = new HistoryListAdapter(getApplicationContext(), list);
        listview.setAdapter(adapter);
    }
}