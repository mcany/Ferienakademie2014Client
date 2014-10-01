package de.ferienakademie.neverrest.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

import java.util.ArrayList;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.HistoryListAdapter;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by arno on 29/09/14.
 * adapted from http://www.vogella.com/tutorials/AndroidListView/article.html
 */

public class HistoryActivity extends FragmentActivity implements NeverrestInterface {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mListView = (ListView) findViewById(R.id.historyList);

        ArrayList<Challenge> list = new ArrayList<Challenge>();

        for (int i = 0; i < 25; i++) {
            Challenge c = new Challenge();
            c.setTitle("Challange " + i);
            c.setDescription("Description " + i);
            list.add(c);
        }

        final HistoryListAdapter adapter = new HistoryListAdapter(getApplicationContext(), list);
        mListView.setAdapter(adapter);


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