package de.ferienakademie.neverrest.view;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.ActiveChallengesAdapter;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by Christoph on 29.09.2014.
 */


public class ActiveChallengesActivity extends ListActivity {

    List<Challenge> challenges;
    public static final String TAG = ActiveChallengesActivity.class.getSimpleName();
    ActiveChallengesAdapter mAdapter;
    DatabaseHandler mDatabaseHandler;

    protected void onCreate(Bundle  savedInstanceState){
        super.onCreate(savedInstanceState);


        //TODO: delete this, when main activity is called before
        DatabaseUtil.INSTANCE.initialize(this);
        /*mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
        try {
            // how do I get objects from the database? Can I write queries? Now this is null
            challenges = mDatabaseHandler.getChallengeDao().queryForAll();
        }
        catch (SQLException exception) {
            Log.d(TAG, exception.getMessage());
        }*/
        Resources res = getResources();
        Drawable[] icons = new Drawable[] {res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer)};
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
       Intent challengeActivity = new Intent(this, ChallengeActivity.class);
        challengeActivity.putExtra("Challenge", challenges.get(position));
        startActivity(challengeActivity);
    }

    protected void onStart(){
        super.onStart();

    }

    protected void onRestart(){
        super.onRestart();

    }

    protected void onResume(){
        super.onResume();
        mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
        try {
            // how do I get objects from the database? Can I write queries? Now this is null
            challenges = mDatabaseHandler.getChallengeDao().queryForAll();
        }
        catch (SQLException exception) {
            Log.d(TAG, exception.getMessage());
        }
        mAdapter = new ActiveChallengesAdapter(this, activeChallenges());
        setListAdapter(mAdapter);

    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    private List<Challenge> activeChallenges() {
        List<Challenge> activeChallenges = new LinkedList<Challenge>();
        for(Challenge challenge : challenges) {
            if(challenge.getCompletedEffort() > 0) {
                activeChallenges.add(challenge);
            }
        }
        return activeChallenges;
    }


}

