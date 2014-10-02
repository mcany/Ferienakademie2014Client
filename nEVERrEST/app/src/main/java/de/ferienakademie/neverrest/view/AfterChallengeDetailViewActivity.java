package de.ferienakademie.neverrest.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.MetricType;
import de.ferienakademie.neverrest.model.SportsType;

/**
 * Created by Christoph on 01.10.2014.
 */
public class AfterChallengeDetailViewActivity extends FragmentActivity implements View.OnClickListener {

    private de.ferienakademie.neverrest.model.Activity mSportiveActivity;
    private Button mDetailsButton;
    private Button mMenuButton;
    private ImageView mGroupSingleImage;
    private ImageView mSportsTypeImage;
    private TextView mTextViewGained;
    private TextView mTextViewFinishedStage;
    private TextView mTextViewChallengeName;
    private TextView mTextViewSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSportiveActivity = (de.ferienakademie.neverrest.model.Activity) getIntent().getSerializableExtra(Constants.EXTRA_ACTIVITY);

        setContentView(R.layout.activity_after_challenge_detail_view);
        mTextViewChallengeName = (TextView) findViewById(R.id.textViewChallengeName);
        mTextViewChallengeName.setText("Challenge Name");
        mTextViewGained = (TextView) findViewById(R.id.textViewGained);
        mTextViewGained.setText("You gained: " + ((int ) (Math.random() * 10000)) + " ms additional lifetime\nAnd burned " + ((int) (Math.random() * 100000)) + " calories.");
		// mTextViewGained.setText("You gained: " + mSportiveActivity.getAdditionalLifetimeInMilliseconds() + " ms additional lifetime\nAnd burned " + mSportiveActivity.getConsumptedEnergyInCalories() + " calories.");
        mTextViewFinishedStage = (TextView) findViewById(R.id.textViewFinishedStage);
        String unitSports = (mSportiveActivity.getChallenge().getType() == MetricType.HORIZONTALDISTANCE) ? "km" : "altitude meters";
        mTextViewFinishedStage.setText("Results:\n" + " "+ unitSports + " \n" + mSportiveActivity.getDuration()/60000.0 + " min");
        mTextViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        mTextViewSpeed.setText("Average speed: \n" + 50.0/(mSportiveActivity.getDuration() / 360000.0) + " " + unitSports +"/h");

        mDetailsButton = (Button) findViewById(R.id.buttonDetails);
        mMenuButton = (Button) findViewById(R.id.buttonMenu);

        mSportsTypeImage = (ImageView) findViewById(R.id.imageSportType);
        mGroupSingleImage = (ImageView) findViewById(R.id.imageChallengeTypeGroup);

        Drawable iconSportsType = (mSportiveActivity.getSportsType() == SportsType.CYCLING) ? this.getResources().getDrawable(R.drawable.biking) : (mSportiveActivity.getSportsType() == SportsType.HIKING) ? this.getResources().getDrawable(R.drawable.walking) : this.getResources().getDrawable(R.drawable.running);
        mSportsTypeImage.setImageDrawable(iconSportsType);

        Drawable iconChallengeGroup =  this.getResources().getDrawable(R.drawable.single);
        mGroupSingleImage.setImageDrawable(iconChallengeGroup);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.after_challenge_detail_view, menu);
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
        switch(view.getId()) {
            case R.id.buttonDetails:
                Toast.makeText(this, "Ich habe nicht die Absicht Details anzuzeigen...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonMenu:
                Intent intent = new Intent(this,FinishedChallengeActivity.class);
                intent.putExtra(Constants.EXTRA_ACTIVITY, mSportiveActivity);
                startActivity(intent);
                break;
        }

    }
}
