package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.MetricType;
import de.ferienakademie.neverrest.model.SportsType;

import static android.view.View.OnClickListener;

public class ChallengeActivity extends FragmentActivity
        implements OnClickListener, NeverrestInterface {

    private Challenge mChallenge;
    private TextView mHeading;
    private ImageView mChallengeImage;
    private TextView mDetailsTextView;
    private Button mStartButton;
    private Button mAbortButton;

    ///////// NAVIGATION DRAWER STUFF /////////
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int mDrawerPosition;
    private boolean mIsCreated;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static final String TAG = ChallengeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        mChallenge = (Challenge) getIntent().getSerializableExtra(Constants.EXTRA_CHALLENGE);

        mHeading = (TextView) findViewById(R.id.heading);
        mHeading.setText(mChallenge.getTitle());
        mStartButton = (Button) findViewById(R.id.buttonStart);
        mAbortButton = (Button) findViewById(R.id.buttonAbort);
        mStartButton.setOnClickListener(this);
        mAbortButton.setOnClickListener(this);

        mIsCreated = true;
        setUpNavigationDrawer();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBarImage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        LinearLayout linearLayoutProgressBar = (LinearLayout) findViewById(R.id.linearLayoutProgressBar);
        linearLayoutProgressBar.setMinimumHeight(size.x);
        ViewGroup.LayoutParams params = linearLayoutProgressBar.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (int) (size.x * 0.5);
        params.width = (int) (size.x * 0.86);
        progressBar.setMax((int) mChallenge.getTotalEffort());
        progressBar.setProgress((int) (mChallenge.getTotalEffort() - mChallenge.getCompletedEffort()));
		progressBar.setBackgroundResource(mChallenge.getIconResourceId());
        mDetailsTextView = (TextView) findViewById(R.id.textViewDetails);
        //List<Activity> activitiesOfChallange = DatabaseUtil.INSTANCE.getDatabaseHandler().getActivityDao().queryForEq(de.ferienakademie.neverrest.model.Activity.C)
        //int duration =
        String unit = (mChallenge.getType() == MetricType.HORIZONTALDISTANCE) ? " km" : " m";
        mDetailsTextView.setText(mChallenge.getCompletedEffort() + " of " + mChallenge.getTotalEffort() + unit + "\n" );
        if(mChallenge.getType() == MetricType.HORIZONTALDISTANCE) {
            linearLayoutProgressBar.setRotation(0);
        }
        Drawable iconChallenge = (mChallenge.getType() == MetricType.HORIZONTALDISTANCE) ? this.getResources().getDrawable(R.drawable.distance) : this.getResources().getDrawable(R.drawable.altitude);
        ((ImageView)findViewById(R.id.imageChallengeType)).setImageDrawable(iconChallenge);
        Drawable iconChallengeGroup =  this.getResources().getDrawable(R.drawable.single);
        ((ImageView)findViewById(R.id.imageChallengeTypeGroup)).setImageDrawable(iconChallengeGroup);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.mChallenge, menu);
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
        switch (view.getId()) {
            case R.id.buttonStart:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select your kind of sport");

                AlertDialog.Builder activity = builder.setItems(new CharSequence[]
                                {"Running", "Biking", "Hiking"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                Intent intent = new Intent(ChallengeActivity.this, MainMenuActivity.class);

                                switch (which) {
                                    case 0:
                                        intent.putExtra(MainMenuActivity.SPORTS_TYPE, SportsType.RUNNING);
                                        break;
                                    case 1:
                                        intent.putExtra(MainMenuActivity.SPORTS_TYPE, SportsType.CYCLING);
                                        break;
                                    case 2:
                                        intent.putExtra(MainMenuActivity.SPORTS_TYPE, SportsType.HIKING);
                                        break;
                                }
                                startActivity(intent);

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
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // Yes button clicked
                                DatabaseHandler databaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
                                try {
                                    databaseHandler.getChallengeDao().delete(mChallenge);
                                    List<Challenge> allChallengesTest = databaseHandler.getChallengeDao().queryForAll();
                                } catch (SQLException exception) {
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

    @Override
    public void setUpNavigationDrawer() {
        mDrawerPosition = getIntent().getIntExtra(Constants.EXTRA_POSITION, -1);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer_challenge);
        mNavigationDrawerFragment.setPosition(mDrawerPosition);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_challenge,
                (DrawerLayout) findViewById(R.id.drawer_layout_challenge));
        onNavigationDrawerItemSelected(mDrawerPosition);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mDrawerPosition = position;

        if (mIsCreated) {
            // update the main content by replacing fragments
            switch (position) {
                case 0:
                    mTitle = getString(R.string.title_navigation_main_menu);
                    Intent mainIntent = new Intent(this, FindChallengesActivity.class);
                    mainIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(mainIntent);
                    this.finish();
                    break;
                case 1:
                    mTitle = getString(R.string.title_navigation_profile);
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    profileIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(profileIntent);
                    this.finish();
                    break;
                case 2:
                    mTitle = getString(R.string.title_navigation_challenges);
                    break;
            }
        }

    }

    @Override
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}
