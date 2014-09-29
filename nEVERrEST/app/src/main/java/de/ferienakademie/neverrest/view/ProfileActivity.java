package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseUtil;

/**
 * Created by arno on 29/09/14.
 */
public class ProfileActivity extends Activity {

    // user
    TextView userName;
    TextView userSize;
    TextView userWeight;
    TextView userGender;
    TextView userAge;
    // stats
    TextView distanceVertical;
    TextView distanceHorizotal;

    // button
    Button wholeHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initialize textViews for user
        userName = (TextView) findViewById(R.id.userName);
        userSize = (TextView) findViewById(R.id.userSize);
        userWeight = (TextView) findViewById(R.id.userWeight);
        userGender = (TextView) findViewById(R.id.userGender);
        userAge = (TextView) findViewById(R.id.userAge);
        // ... and stats
        distanceVertical = (TextView) findViewById(R.id.distancesVertical);
        distanceHorizotal = (TextView) findViewById(R.id.distancesHorizontal);

        wholeHistory = (Button) findViewById(R.id.wholeHistoryButton);

        // set placeholders for user
        userName.setText(String.format(userName.getText().toString(), "FitFritz"));
        userSize.setText(String.format(userSize.getText().toString(), 180));
        userWeight.setText(String.format(userWeight.getText().toString(), 75));
        userGender.setText(String.format(userGender.getText().toString(), "male"));
        userAge.setText(String.format(userAge.getText().toString(), 42));
        // ... and stats
        distanceVertical.setText(String.format(distanceVertical.getText().toString(), 2, 32));
        distanceHorizotal.setText(String.format(distanceHorizotal.getText().toString(), 250, 200));

        wholeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Congratulations, you pressed a button.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
