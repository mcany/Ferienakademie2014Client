package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.shared.beans.Challenge;

public class ChallengeActivity extends Activity {

    Challenge challenge;
    TextView heading;
    ImageView challengeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        challenge = (Challenge) savedInstanceState.get("Challenge");
        //Challenge dummyChallenge = new Challenge("idsaf", "Roberti Golumm", de.ferienakademie.neverrest.shared.beans.Activity.Type.CYCLING,"des",100.0,100);
        //challenge = dummyChallenge;
        heading = (TextView) findViewById(R.id.heading);
        heading.setText(challenge.getTitle());
     //   challenge.getPercentageCompleted();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.challenge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
