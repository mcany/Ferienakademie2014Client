package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;

import static android.view.View.OnClickListener;
import static de.ferienakademie.neverrest.view.NavigationDrawerFragment.NavigationDrawerCallbacks;

public class ProfileActivity extends FragmentActivity
        implements NeverrestInterface, OnClickListener, NavigationDrawerCallbacks {

    public static final String TAG = ProfileActivity.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";

    ///////// NAVIGATION DRAWER STUFF /////////

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    ///////// NAVIGATION DRAWER STUFF /////////
    private CharSequence mTitle;
    private int mDrawerPosition;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private boolean mIsCreated;
    private DatabaseHandler mDatabaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mIsCreated = true;
        mTitle = getTitle();

        setUpNavigationDrawer();

        // Initialize database
        DatabaseUtil.INSTANCE.initialize(getApplicationContext());
        mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "requested");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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
    public void onNavigationDrawerItemSelected(int position) {
        mDrawerPosition = position;

        if (mIsCreated) {

            // update the main content by replacing fragments
            switch (position) {
                case 0:
                    Intent mainIntent = new Intent(this, MainMenuActivity.class);
                    mainIntent.putExtra("POSITION", position);
                    startActivity(mainIntent);
                    this.finish();
                    break;
                case 1:
                    mTitle = getString(R.string.title_navigation_profile);
                    break;
                case 2:
                    Intent challengeIntent = new Intent(this, ChallengeActivity.class);
                    challengeIntent.putExtra("POSITION", position);
                    startActivity(challengeIntent);
                    this.finish();
                    break;
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setUpNavigationDrawer() {
        mDrawerPosition = getIntent().getIntExtra("POSITION", 0);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer_profile);
        mNavigationDrawerFragment.setPosition(mDrawerPosition);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_profile,
                (DrawerLayout) findViewById(R.id.drawer_layout_profile));
        onNavigationDrawerItemSelected(mDrawerPosition);
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.ferienakademie.neverrest.R;

/**
 * Created by arno on 29/09/14.
 */
public class ProfileActivity extends Activity {

    // user
    TextView userName;
    TextView userGender;
    TextView userAge;
    TextView userSize;
    TextView userWeight;
    // stats
    TextView totalDistance;
    TextView totalAltitude;

    // image view
    ImageView userAvatar;

    // linear layouts
    LinearLayout userData;
    LinearLayout badgeLayout;
    LinearLayout globalStatistics;

    public LinearLayout createBadge(int image, String text, Context context) {
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_small);

        LinearLayout layout = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, 0, padding, 0);

        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.badge_size_small), getResources().getDimensionPixelSize(R.dimen.badge_size_small));
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(image);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(textParams);
        textView.setText(text);

        layout.addView(imageView);
        layout.addView(textView);

        return layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userAvatar = (ImageView) findViewById(R.id.userAvatar);
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Editing avatar...", Toast.LENGTH_SHORT).show();
            }
        });
        userName = (TextView) findViewById(R.id.userName);
        userGender = (TextView) findViewById(R.id.userGender);
        userAge = (TextView) findViewById(R.id.userAge);
        userSize = (TextView) findViewById(R.id.userSize);
        userWeight = (TextView) findViewById(R.id.userWeight);
        // ... and stats
        totalDistance = (TextView) findViewById(R.id.totalDistance);
        totalAltitude = (TextView) findViewById(R.id.totalAltitude);
        // linear layout
        badgeLayout = (LinearLayout) findViewById(R.id.lastBadges);
        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Editing badges...", Toast.LENGTH_SHORT).show();
            }
        });
        for (int i = 0; i < 8; i++) {
            badgeLayout.addView(createBadge(R.drawable.sampleimage, "Badge " + i, this));
        }
        userData = (LinearLayout) findViewById(R.id.userData);
        userData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Editing user data...", Toast.LENGTH_SHORT).show();
            }
        });
        globalStatistics = (LinearLayout) findViewById(R.id.globalStatistics);
        globalStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Showing details for global data...", Toast.LENGTH_SHORT).show();
            }
        });

        // set placeholders for user
        userName.setText(String.format(userName.getText().toString(), "FitFritz"));
        userGender.setText(String.format(userGender.getText().toString(), "male"));
        userAge.setText(String.format(userAge.getText().toString(), 42));
        userSize.setText(String.format(userSize.getText().toString(), 180));
        userWeight.setText(String.format(userWeight.getText().toString(), 75));
        // ... and stats
        totalDistance.setText(String.format(totalDistance.getText().toString(), "biking: 12km, hiking: 23km, running: 14km"));
        totalAltitude.setText(String.format(totalAltitude.getText().toString(), "biking: 854km, hiking: 451km, running: 673km"));
    }
}
