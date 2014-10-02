package de.ferienakademie.neverrest.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.controller.HistoryListAdapter;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by arno on 29/09/14.
 * adapted from http://www.vogella.com/tutorials/AndroidListView/article.html
 */

public class HistoryActivity extends FragmentActivity implements NeverrestInterface {

    private DatabaseHandler mDatabaseHandler;
    private Context context = this;

    //TODO: implement the group challenges (already some code provided)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //initialize database
        DatabaseUtil.INSTANCE.initialize(getApplicationContext());
        mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();

        //get list of all finished challenges from the database
        List<Challenge> finishedSingleChallenges = new ArrayList<Challenge>();
        finishedSingleChallenges.clear();
        try {
            finishedSingleChallenges = mDatabaseHandler.getChallengeDao().queryForEq("finished", true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //add all finished challenges to the listview and set the according adapter
        final ListView listviewSingle = (ListView) findViewById(R.id.historyListSingle);
        //ListView listviewGroup = (ListView) findViewById(R.id.historyListGroup);

        final ArrayList<Challenge> listSingle = new ArrayList<Challenge>();
        //ArrayList<Challenge> listGroup = new ArrayList<Challenge>();

        for (int i = 0; i < finishedSingleChallenges.size(); i++) {
            listSingle.add(finishedSingleChallenges.get(i));
        }

        final HistoryListAdapter adapterSingle = new HistoryListAdapter(getApplicationContext(), listSingle);
        listviewSingle.setAdapter(adapterSingle);

        //click on a finished challenge opens the challenge details
        listviewSingle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(context, HistoryDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_CHALLENGE, listSingle.get(position));
                startActivity(intent);
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