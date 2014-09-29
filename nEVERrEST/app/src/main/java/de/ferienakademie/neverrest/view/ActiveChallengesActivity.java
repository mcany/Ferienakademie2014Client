package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import de.ferienakademie.neverrest.R;

/**
 * Created by Christoph on 29.09.2014.
 */
public class ActiveChallengesActivity extends ListActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String [] values = new String [] {"bal", "MT Eve", "Root 66"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.active_challenge_item, R.id.firstLine, values);
        setListAdapter(adapter);
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

