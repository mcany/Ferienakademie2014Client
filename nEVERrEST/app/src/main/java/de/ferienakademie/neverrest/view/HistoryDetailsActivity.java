package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Challenge;

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
    ImageView image;

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
        image = (ImageView) findViewById(R.id.challengeBadge);

        Challenge challenge = (Challenge)getIntent().getSerializableExtra(Constants.EXTRA_CHALLENGE);

        // set placeholders for finished challenge
        name.setText(String.format(name.getText().toString(), challenge.getTitle()));
        value.setText(String.format(value.getText().toString(), challenge.getTotalEffort()));
        sports.setText(String.format(sports.getText().toString(), challenge.getType()));
        start.setText(String.format(start.getText().toString(), challenge.getTimestampStarted()));
        finish.setText(String.format(finish.getText().toString(), challenge.getTimestampLastModified()));
        duration.setText(String.format(duration.getText().toString(), challenge.getTimestampLastModified()-challenge.getTimestampStarted()));
        image.setImageDrawable(this.getResources().getDrawable((challenge.getIconResourceId())));
        //TODO: display different durations of the different sport types used to finish the challenge
        //durationDetails.setText(String.format(durationDetails.getText().toString(), "7h bike, 10h etc"));
    }

}