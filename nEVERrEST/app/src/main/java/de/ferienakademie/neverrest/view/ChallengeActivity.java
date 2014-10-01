package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.Challenge;

public class ChallengeActivity extends Activity implements View.OnClickListener  {

    Challenge challenge;
    TextView heading;
    ImageView challengeImage;
    Button startButton;
    Button abortButton;
    public static final String TAG = ChallengeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
         challenge = (Challenge) getIntent().getSerializableExtra("Challenge");

        //Challenge dummyChallenge = new Challenge("idsaf", "Roberti Golumm", de.ferienakademie.neverrest.shared.beans.Activity.Type.CYCLING,"des",100.0,100);
        //challenge = dummyChallenge;
        heading = (TextView) findViewById(R.id.heading);
        heading.setText(challenge.getTitle());
        startButton = (Button) findViewById(R.id.buttonStart);
        abortButton = (Button) findViewById(R.id.buttonAbort);
        startButton.setOnClickListener(this);
        abortButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonStart:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select your kind of sport");

                AlertDialog.Builder activity = builder.setItems(new CharSequence[]
                                {"Running", "Biking", "Hiking"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                Intent aktivitatActivity = new Intent(ChallengeActivity.this, AktivitaetsActivity.class);

                                switch (which) {
                                    case 0:
                                        aktivitatActivity.putExtra("Activity", "mit Challenge jar setzen");
                                        break;
                                    case 1:
                                        aktivitatActivity.putExtra("Activity", "mit Challenge jar setzen");
                                        break;
                                    case 2:
                                        aktivitatActivity.putExtra("Activity", "auch oben die Buttons");
                                        break;
                                }
                                startActivity(aktivitatActivity);

                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                break;
            case R.id.buttonAbort:
                // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-in-android
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                // Yes button clicked
                                DatabaseHandler databaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
                                try {
                                    databaseHandler.getChallengeDao().delete(challenge);
                                    List<Challenge> allChallengesTest = databaseHandler.getChallengeDao().queryForAll();
                                }
                                catch (SQLException exception){
                                    Log.d(TAG, exception.getMessage());
                                }
                                ChallengeActivity.this.finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builderYesNo = new AlertDialog.Builder(this);
                builderYesNo.setMessage("Do you really want to give up?").setPositiveButton("Yes, I'm a looser.", dialogClickListener)
                        .setNegativeButton("No, I'll finish it.", dialogClickListener).show();
              //
                break;

        }
    }
}
