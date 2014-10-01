package de.ferienakademie.neverrest.view;


import android.app.Dialog;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.MetricType;

public class FindChallengesActivity extends FragmentActivity implements NeverrestInterface {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static final String TAG = FindChallengesActivity.class.getSimpleName();
    final int notClickedMarkerImage = R.drawable.ic_launcher;
    final int clickedMarkerImage = R.drawable.ic_launcher;

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


    //continents coordinates
    final LatLng coordinatesAfrica = new LatLng(0.2136714, 16.98485);
    final LatLng coordinatesEurope = new LatLng(49.5, 22);
    final LatLng coordinatesAsia = new LatLng(29, 100);
    final LatLng coordinatesSouthAmerica = new LatLng(-21.7351043, -63.28125);
    final LatLng coordinatesNorthAmerica = new LatLng(37.5, -110);
    final LatLng coordinatesAustralia = new LatLng(-26.4390742, 133.2813229);
    final LatLng coordinatesAntarctica = new LatLng(-75, 0);

    //continents markers
    private Marker markerAfrica = null;
    private Marker markerEurope = null;
    private Marker markerAsia = null;
    private Marker markerSouthAmerica = null;
    private Marker markerNorthAmerica = null;
    private Marker markerAustralia = null;
    private Marker markerAntarctica = null;

    //continents challenges
    private List<Challenge> challengesAfrica = new ArrayList<Challenge>();
    private List<Challenge> challengesEurope = new ArrayList<Challenge>();
    private List<Challenge> challengesAsia = new ArrayList<Challenge>();
    private List<Challenge> challengesSouthAmerica = new ArrayList<Challenge>();
    private List<Challenge> challengesNorthAmerica = new ArrayList<Challenge>();
    private List<Challenge> challengesAustralia = new ArrayList<Challenge>();
    private List<Challenge> challengesAntarctica = new ArrayList<Challenge>();


    //dummy challenges for Africa
    private LatLng[] dummyChallengesAfrica = {new LatLng(28.0289837, 1.6666663), new LatLng(31.7945869, -7.0849336), new LatLng(-28.4792625, 24.6727135),
            new LatLng(-18.7792678, 46.8344597), new LatLng(-4.0335162, 21.7500603)};


    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_challenges);

        mIsCreated = true;

        setUpNavigationDrawer();
        setUpMapIfNeeded();
        resetMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onBackPressed() {
        if (mMap.getMaxZoomLevel() > 2.0f) {
            mMap.clear();
            resetMap();
        } else {
            super.onBackPressed();
        }
    }

    private void resetMap() {
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        //initialize the first position of the camera
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesAfrica, 2.0f)));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        setUpMap();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFindChallenges))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnInfoWindowClickListener(new FindChallengesActivityOnInfoWindowClickListener());
                mMap.setOnMarkerClickListener(new FindChallengesActivityOnMarkerClickListener());
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        //TODO: get challenges from the DB
        DatabaseUtil.INSTANCE.initialize(getApplicationContext());
        DatabaseHandler databaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
        try {
            Iterator<Challenge> mChallengeIterator = databaseHandler.getChallengeDao().iterator();
            while (mChallengeIterator.hasNext()) {
                Challenge mChallenge = mChallengeIterator.next();
                challengesAfrica.add(mChallenge);
            }
        } catch (SQLException exception) {
            Log.d(TAG, exception.getMessage());
        }

        //create continent markers
        markerAfrica = mMap.addMarker(new MarkerOptions().position(coordinatesAfrica).title(""+ dummyChallengesAfrica.length));

        //markerAfrica.showInfoWindow();
        markerAfrica.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));

        markerEurope = mMap.addMarker(new MarkerOptions().position(coordinatesEurope).title("" + dummyChallengesAfrica.length));
        markerEurope.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));

        markerAsia = mMap.addMarker(new MarkerOptions().position(coordinatesAsia).title("" + dummyChallengesAfrica.length));
        markerAsia.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));

        markerNorthAmerica = mMap.addMarker(new MarkerOptions().position(coordinatesNorthAmerica).title("" + dummyChallengesAfrica.length));
        markerNorthAmerica.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));

        markerSouthAmerica = mMap.addMarker(new MarkerOptions().position(coordinatesSouthAmerica).title("" + dummyChallengesAfrica.length));
        markerSouthAmerica.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));

        markerAustralia = mMap.addMarker(new MarkerOptions().position(coordinatesAustralia).title("" + dummyChallengesAfrica.length));
        markerAustralia.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));

        markerAntarctica = mMap.addMarker(new MarkerOptions().position(coordinatesAntarctica).title("" + dummyChallengesAfrica.length));
        markerAntarctica.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(notClickedMarkerImage, "")));


        //TODO: Change the image of the marker

        //Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        //Bitmap bmp = Bitmap.createBitmap(200, 50, conf);
        //Canvas canvas = new Canvas(bmp);

        //Paint paint =  new Paint();

        //canvas.drawText("TEXT", 0, 50, paint); // paint defines the text color, stroke width, size
        //mMap.addMarker(new MarkerOptions()
        //                .position(new LatLng(0,0))
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2))
        //                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
        //                .anchor(0.5f, 1)
        //).setVisible(true);


        //Marker myLocMarker = mMap.addMarker(new MarkerOptions()
        //        .position(new LatLng(0,0))
        //        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.sampleimage, "your text goes here"))));
    }


    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }


    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    @Override
    public void setUpNavigationDrawer() {
        mDrawerPosition = 0;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer_find_challenges);
        mNavigationDrawerFragment.setPosition(mDrawerPosition);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_find_challenges,
                (DrawerLayout) findViewById(R.id.drawer_layout_find_challenges));
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
                    break;
                case Constants.ACTIVITY_PROFILE:
                    mTitle = getString(R.string.title_navigation_profile);
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    profileIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(profileIntent);
                    this.finish();
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
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    class FindChallengesActivityOnMarkerClickListener implements GoogleMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //marker.setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(clickedMarkerImage, marker.getTitle())));
            // custom dialog
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_dialog_map_challenge_info);
            dialog.setTitle(marker.getTitle());

            // set the custom dialog components - text, image and button
            TextView description = (TextView) dialog.findViewById(R.id.challengeDescription);
            description.setText("Android custom dialog example!");
            TextView details = (TextView) dialog.findViewById(R.id.challengeDetails);
            details.setText("foo bar foo bar foo bar");
            ImageView image = (ImageView) dialog.findViewById(R.id.challengeImage);
            image.setImageResource(R.drawable.ic_launcher);


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonAcceptChallenge);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   Intent intent = new Intent(context, ChallengeActivity.class);
                   //intent.putExtra("Challenge", marker.getTitle());
                   startActivity(intent);
                }
            });

           dialog.show();

            return false;
        }
    }

    class FindChallengesActivityOnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick(Marker marker) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(marker.getPosition(), 3.0f)));
            marker.setVisible(false);

            if (marker.getPosition().equals(coordinatesAfrica)) {

                for (LatLng coordinateOfTheChallenge : dummyChallengesAfrica) {
                    mMap.addMarker(new MarkerOptions().position(coordinateOfTheChallenge).title("Challenge Mt. Everest"));
                }

                ListIterator<Challenge> challengeListIterator = challengesAfrica.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            } else if (marker.getPosition().equals(coordinatesEurope)) {
                ListIterator<Challenge> challengeListIterator = challengesEurope.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            } else if (marker.getPosition().equals(coordinatesAsia)) {
                ListIterator<Challenge> challengeListIterator = challengesAsia.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            } else if (marker.getPosition().equals(coordinatesNorthAmerica)) {
                ListIterator<Challenge> challengeListIterator = challengesNorthAmerica.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            } else if (marker.getPosition().equals(coordinatesSouthAmerica)) {
                ListIterator<Challenge> challengeListIterator = challengesSouthAmerica.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            } else if (marker.getPosition().equals(coordinatesAustralia)) {
                ListIterator<Challenge> challengeListIterator = challengesAustralia.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            } else if (marker.getPosition().equals(coordinatesAntarctica)) {
                ListIterator<Challenge> challengeListIterator = challengesAntarctica.listIterator();
                while (challengeListIterator.hasNext()) {
                    Challenge mChallenge = challengeListIterator.next();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mChallenge.getStartingLatitude(), mChallenge.getStartingLongitude()))
                            .title(mChallenge.getTitle()));
                }
            }
            else {
            }

        }
    }
}