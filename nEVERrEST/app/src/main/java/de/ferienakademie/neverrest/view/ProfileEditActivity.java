package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.User;

import static android.view.View.OnClickListener;

/**
 * Created by arno on 29/09/14.
 */
public class ProfileEditActivity extends FragmentActivity
        implements NeverrestInterface, OnClickListener {

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

    // user
    EditText userNameEdit;
    Spinner userGenderEdit;
    EditText userAgeEdit;
    EditText userSizeEdit;
    EditText userWeightEdit;
    EditText userSessionsEdit;

    // button
    Button cancelButton;
    Button saveButton;

    // the current user
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        mIsCreated = true;
        mTitle = getTitle();

        setUpNavigationDrawer();

        // initialize edittexts for user
        userNameEdit = (EditText) findViewById(R.id.userNameEdit);
        userGenderEdit = (Spinner) findViewById(R.id.userGenderEdit);
        userAgeEdit = (EditText) findViewById(R.id.userAgeEdit);
        userSizeEdit = (EditText) findViewById(R.id.userSizeEdit);
        userWeightEdit = (EditText) findViewById(R.id.userWeightEdit);
        userSessionsEdit = (EditText) findViewById(R.id.userSessionsEdit);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        // add genders to spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner_gender);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userGenderEdit.setAdapter(adapter);

        try {
            if (DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().queryForEq("uuid", ProfileActivity.USER_UUID) != null &&
                    DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().queryForEq("uuid", ProfileActivity.USER_UUID).size() > 0) {
                currentUser = DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().queryForEq("uuid", ProfileActivity.USER_UUID).get(0);
                userNameEdit.setText(currentUser.getUsername());
                if (currentUser.getMale() == true) {
                    userGenderEdit.setSelection(0);
                } else {
                    userGenderEdit.setSelection(1);
                }
                userAgeEdit.setText("" + (int) currentUser.getAge());
                userSizeEdit.setText("" + (int) currentUser.getHeight());
                userWeightEdit.setText("" + (int) currentUser.getMass());
                userSessionsEdit.setText("" + currentUser.getEstimatedTrainingSessionsPerWeek());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileEditActivity.this, "Saved stuff...", Toast.LENGTH_SHORT).show();
                User currentUser = null;
                try {
                    currentUser = DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().queryForEq("uuid", ProfileActivity.USER_UUID).get(0);
                    currentUser.setUsername(userNameEdit.getText().toString());
                    currentUser.setMale(userGenderEdit.getSelectedItemPosition() == 0 ? true : false);
                    currentUser.setAge(Integer.valueOf(userAgeEdit.getText().toString()));
                    currentUser.setHeight(Integer.valueOf(userSizeEdit.getText().toString()));
                    currentUser.setMass(Integer.valueOf(userWeightEdit.getText().toString()));
                    currentUser.setEstimatedTrainingSessionsPerWeek(Integer.valueOf(userSessionsEdit.getText().toString()));
                    DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().update(currentUser);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                case Constants.ACTIVITY_MAIN_MENU:
                    Intent mainIntent = new Intent(this, FindChallengesActivity.class);
                    mainIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(mainIntent);
                    this.finish();
                    break;
                case Constants.ACTIVITY_PROFILE:
                    mTitle = getString(R.string.title_navigation_profile);
                    break;
                case Constants.ACTIVITY_CHALLENGE_OVERVIEW:
                    Intent challengeIntent = new Intent(this, ActiveChallengesActivity.class);
                    challengeIntent.putExtra(Constants.EXTRA_POSITION, position);
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
        mDrawerPosition = getIntent().getIntExtra(Constants.EXTRA_POSITION, 0);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer_profile);
        //mNavigationDrawerFragment.setPosition(mDrawerPosition);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_profile,
                (DrawerLayout) findViewById(R.id.drawer_layout_profile));
        //onNavigationDrawerItemSelected(mDrawerPosition);
    }
}
