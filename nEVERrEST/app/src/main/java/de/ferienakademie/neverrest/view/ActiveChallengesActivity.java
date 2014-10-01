package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.ActiveChallengesAdapter;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.Challenge;

import static android.widget.AdapterView.OnItemClickListener;

/**
 * Created by Christoph on 29.09.2014.
 */


public class ActiveChallengesActivity extends FragmentActivity implements NeverrestInterface,
        OnItemClickListener {

    List<Challenge> challenges;
    public static final String TAG = ActiveChallengesActivity.class.getSimpleName();
    ActiveChallengesAdapter mAdapter;
    DatabaseHandler mDatabaseHandler;
    private ListView mListView;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_challenges);
        mListView = (ListView) findViewById(R.id.list_view_active_challenges);

        mIsCreated = true;
        setUpNavigationDrawer();

        //TODO: delete this, when main activity is called before
        DatabaseUtil.INSTANCE.initialize(this);
        /*mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
        try {
            // how do I get objects from the database? Can I write queries? Now this is null
            challenges = mDatabaseHandler.getChallengeDao().queryForAll();
        }
        catch (SQLException exception) {
            Log.d(TAG, exception.getMessage());
        }*/


    }

    protected void onStart() {
        super.onStart();

    }

    protected void onRestart() {
        super.onRestart();

    }

    protected void onResume() {
        super.onResume();
        mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
        try {
            // how do I get objects from the database? Can I write queries? Now this is null
            challenges = mDatabaseHandler.getChallengeDao().queryForAll();
        } catch (SQLException exception) {
            Log.d(TAG, exception.getMessage());
        }
        mAdapter = new ActiveChallengesAdapter(this, challenges);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setUpNavigationDrawer() {
        mDrawerPosition = getIntent().getIntExtra(Constants.EXTRA_POSITION, 0);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer_active_challenges);
        mNavigationDrawerFragment.setPosition(mDrawerPosition);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_active_challenges,
                (DrawerLayout) findViewById(R.id.drawer_layout_active_challenges));
        onNavigationDrawerItemSelected(mDrawerPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mDrawerPosition = position;

        if (mIsCreated) {
            // update the main content by replacing fragments
            switch (position) {
                case Constants.ACTIVITY_MAIN_MENU:
                    mTitle = getString(R.string.title_navigation_main_menu);
                    Intent mainIntent = new Intent(this, MainMenuActivity.class);
                    mainIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(mainIntent);
                    this.finish();
                    break;
                case Constants.ACTIVITY_PROFILE:
                    mTitle = getString(R.string.title_navigation_profile);
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    profileIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(profileIntent);
                    this.finish();
                    break;
                case Constants.ACTIVITY_CHALLENGE_OVERVIEW:
                    mTitle = getString(R.string.title_activity_challenge);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, ChallengeActivity.class);
        intent.putExtra(Constants.EXTRA_CHALLENGE, challenges.get(position));
        intent.putExtra(Constants.EXTRA_POSITION, mDrawerPosition);
        startActivity(intent);

    }
}

