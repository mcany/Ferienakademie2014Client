package ferienakademie.de.fitfritz.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ferienakademie.de.fitfritz.R;
import ferienakademie.de.fitfritz.controller.GPSService;


public class StartScreen extends Activity {

    private boolean gpsEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        startGPS();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopGPS();
    }


    protected void startGPS() {
        if (!gpsEnabled) {
            gpsEnabled = true;
            startService(new Intent(this, GPSService.class));
        }
    }


    protected void stopGPS() {
        if (gpsEnabled) {
            gpsEnabled = false;
            stopService(new Intent(this, GPSService.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
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
}
