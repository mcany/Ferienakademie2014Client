package de.ferienakademie.neverrest.view;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.Date;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.ActiveChallengesAdapter;
import de.ferienakademie.neverrest.model.Challenge;

/**
 * Created by Christoph on 29.09.2014.
 */


public class ActiveChallengesActivity extends ListActivity {

    Challenge[] challenges;

    protected void onCreate(Bundle  savedInstanceState){
        super.onCreate(savedInstanceState);
        Challenge dummyChallenge = new Challenge("Robeeeeert","test", 1 );
        challenges[0] = dummyChallenge;

        String [] challenges = new String [] {"Ork", "Gollum", "Root 66", "Pluto"};
        Date[] date = new Date [] {new Date(97,1,15), new Date(14,3,5), new Date(14,5,3), new Date(14,9, 21), new Date()};
        int [] progress = {10, 20, 0, 100};
        Resources res = getResources();
        Drawable[] icons = new Drawable[] {res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer)};
        ActiveChallengesAdapter adapter = new ActiveChallengesAdapter(this, challenges, icons, progress, date);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
       Intent challengeActivity = new Intent(this, ChallengeActivity.class);
        challengeActivity.putExtra("Challenge", challenges[0]);
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
}

