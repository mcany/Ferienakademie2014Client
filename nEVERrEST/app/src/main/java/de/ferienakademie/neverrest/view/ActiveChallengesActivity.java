package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.ActiveChallengesAdapter;

/**
 * Created by Christoph on 29.09.2014.
 */
public class ActiveChallengesActivity extends ListActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String [] challenges = new String [] {"Ork", "Gollum", "Root 66", "Pluto"};
        Date[] date = new Date [] {new Date(97,1,15), new Date(14,3,5), new Date(14,5,3), new Date(14,9, 21)};
        int [] progress = {10, 20, 0, 100};
        Resources res = getResources();
        Drawable[] icons = new Drawable[] {res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer),res.getDrawable(R.drawable.ic_drawer)};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.active_challenge_item, R.id.firstLine, values);
        ActiveChallengesAdapter adapter = new ActiveChallengesAdapter(this, challenges, icons, progress, date);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    protected void onStart(){

    }

    protected void onRestart(){

    }

    protected void onResume(){

    }

    protected void onPause(){

    }

    protected void onStop(){

    }

    protected void onDestroy(){

    }
}

