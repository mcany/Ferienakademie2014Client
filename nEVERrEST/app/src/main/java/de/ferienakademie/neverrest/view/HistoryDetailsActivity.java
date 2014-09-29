package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import de.ferienakademie.neverrest.R;

/**
 * Created by arno on 29/09/14.
 */
public class HistoryDetailsActivity extends Activity {

    TextView name;
    TextView value;
    TextView sports;
    TextView start;
    TextView finish;
    TextView duration;
    TextView durationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        // initialize textViews for user
        name = (TextView) findViewById(R.id.challengeName);
        value = (TextView) findViewById(R.id.challengeValue);
        sports = (TextView) findViewById(R.id.challengeSports);
        start = (TextView) findViewById(R.id.challengeStart);
        finish = (TextView) findViewById(R.id.challengeFinish);
        duration = (TextView) findViewById(R.id.challengeDuration);
        durationDetails = (TextView) findViewById(R.id.challengeDurationDetails);

        // set placeholders for user
        name.setText(String.format(name.getText().toString(), "Mt. Everest"));
        value.setText(String.format(value.getText().toString(), "8848m"));
        sports.setText(String.format(sports.getText().toString(), "Hiking"));
        start.setText(String.format(start.getText().toString(), "02.03.2010"));
        finish.setText(String.format(finish.getText().toString(), "24.09.2014"));
        duration.setText(String.format(duration.getText().toString(), "125:52 h"));
        durationDetails.setText(String.format(durationDetails.getText().toString(), "7h bike, 10h etc"));


    }

}
