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
    }
}
