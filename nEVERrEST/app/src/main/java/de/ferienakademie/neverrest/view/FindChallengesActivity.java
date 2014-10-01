package de.ferienakademie.neverrest.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.MetrikType;

public class FindChallengesActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //continents coordinates
    final LatLng coordinatesAfrica = new LatLng(0.2136714,16.98485);
    final LatLng coordinatesEurope = new LatLng(49.5,22);
    final LatLng coordinatesAsia = new LatLng(29,100);
    final LatLng coordinatesSouthAmerica = new LatLng(-21.7351043,-63.28125);
    final LatLng coordinatesNorthAmerica = new LatLng(37.5,-110);
    final LatLng coordinatesAustralia = new LatLng(-26.4390742,133.2813229);
    final LatLng coordinatesAntarctica = new LatLng(-75,0);

    //continents markers
    private Marker markerAfrica = null;
    private Marker markerEurope = null;
    private Marker markerAsia = null;
    private Marker markerSouthAmerica = null;
    private Marker markerNorthAmerica = null;
    private Marker markerAustralia = null;
    private Marker markerAntarctica = null;

    //continents challenges
    private List<Challenge> challengesAfrica = null;
    private List<Challenge> challengesEurope = null;
    private List<Challenge> challengesAsia = null;
    private List<Challenge> challengesSouthAmerica = null;
    private List<Challenge> challengesNorthAmerica = null;
    private List<Challenge> challengesAustralia = null;
    private List<Challenge> challengesAntarctica = null;

    //dummy challenges for Africa
    private LatLng[] dummyChallengesAfrica = {new LatLng(28.0289837,1.6666663),new LatLng(31.7945869,-7.0849336),new LatLng(-28.4792625,24.6727135),
            new LatLng(-18.7792678,46.8344597), new LatLng(-4.0335162,21.7500603)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_challenges);

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
        if(mMap.getMaxZoomLevel() > 2.0f)
        {
            mMap.clear();
            resetMap();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void resetMap(){
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
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
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
                setUpMap();
                mMap.setOnInfoWindowClickListener(new FindChallengesActivityOnInfoWindowClickListener());
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        //TODO: get challenges from the DB

        //create continent markers
        markerAfrica = mMap.addMarker(new MarkerOptions().position(coordinatesAfrica).title(""+ dummyChallengesAfrica.length));
        markerAfrica.showInfoWindow();
        markerEurope = mMap.addMarker(new MarkerOptions().position(coordinatesEurope).title(""+ dummyChallengesAfrica.length));
        markerAsia = mMap.addMarker(new MarkerOptions().position(coordinatesAsia).title(""+ dummyChallengesAfrica.length));
        markerNorthAmerica = mMap.addMarker(new MarkerOptions().position(coordinatesNorthAmerica).title(""+ dummyChallengesAfrica.length));
        markerSouthAmerica = mMap.addMarker(new MarkerOptions().position(coordinatesSouthAmerica).title(""+ dummyChallengesAfrica.length));
        markerAustralia = mMap.addMarker(new MarkerOptions().position(coordinatesAustralia).title(""+ dummyChallengesAfrica.length));
        markerAntarctica = mMap.addMarker(new MarkerOptions().position(coordinatesAntarctica).title(""+ dummyChallengesAfrica.length));


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
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }



    class FindChallengesActivityOnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener
    {

        @Override
        public void onInfoWindowClick(Marker marker) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            if(marker.getPosition().equals(coordinatesAfrica)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesAfrica, 3.0f)));
                markerAfrica.setVisible(false);
                for (LatLng coordinateOfTheChallenge : dummyChallengesAfrica)
                {
                    mMap.addMarker(new MarkerOptions().position(coordinateOfTheChallenge).title("Challenge Mt. Everest"));
                }
            }
            else if(marker.getPosition().equals(coordinatesEurope)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesEurope, 3.0f)));
                markerEurope.setVisible(false);
            }
            else if(marker.getPosition().equals(coordinatesAsia)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesAsia, 3.0f)));
                markerAsia.setVisible(false);
            }
            else if(marker.getPosition().equals(coordinatesNorthAmerica)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesNorthAmerica, 3.0f)));
                markerNorthAmerica.setVisible(false);
            }
            else if(marker.getPosition().equals(coordinatesSouthAmerica)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesSouthAmerica, 3.0f)));
                markerSouthAmerica.setVisible(false);
            }
            else if(marker.getPosition().equals(coordinatesAustralia)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesAustralia, 3.0f)));
                markerAustralia.setVisible(false);
            }
            else if(marker.getPosition().equals(coordinatesAntarctica)) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(coordinatesAntarctica, 3.0f)));
                markerAntarctica.setVisible(false);
            }
            else {
                Challenge dummyChallenge = new Challenge("idsaf", "Roberti Golumm",MetrikType.HORIZONTALDISTANCE,"des","",100.0,0.0,100,0,false);

                Intent intent = new Intent(getBaseContext(),ChallengeActivity.class);
                intent.putExtra("Challenge", dummyChallenge);
                startActivity(intent);
            }
        }
    }
}