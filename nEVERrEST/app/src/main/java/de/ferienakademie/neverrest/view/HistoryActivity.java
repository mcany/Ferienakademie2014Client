package de.ferienakademie.neverrest.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.HistoryListAdapter;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by arno on 29/09/14.
 * adapted from http://www.vogella.com/tutorials/AndroidListView/article.html
 */

public class HistoryActivity extends FragmentActivity implements NeverrestInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        ListView listviewSingle = (ListView) findViewById(R.id.historyListSingle);
        ListView listviewGroup = (ListView) findViewById(R.id.historyListGroup);

        ArrayList<Challenge> listSingle = new ArrayList<Challenge>();
        ArrayList<Challenge> listGroup = new ArrayList<Challenge>();

        for (int i = 0; i < 10; i++) {
            Challenge c = new Challenge();
            c.setTitle("Challenge Single " + i);
            c.setDescription("Description " + i);
            c.setIconPath("drawable://" + R.drawable.sampleimage);
            listSingle.add(c);
        }


        for (int i = 0; i < 10; i++) {
            Challenge c = new Challenge();
            c.setTitle("Challenge Group" + i);
            c.setDescription("Description " + i);
            c.setIconPath("drawable://" + R.drawable.sampleimage);
            listGroup.add(c);
        }

        final HistoryListAdapter adapterSingle = new HistoryListAdapter(getApplicationContext(), listSingle);
        listviewSingle.setAdapter(adapterSingle);

        listviewSingle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(HistoryActivity.this, "pressed challenge: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        final HistoryListAdapter adapterGroup = new HistoryListAdapter(getApplicationContext(), listGroup);
        listviewGroup.setAdapter(adapterGroup);

        listviewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(HistoryActivity.this, "pressed challenge: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setUpNavigationDrawer() {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    public void restoreActionBar() {

    }
}