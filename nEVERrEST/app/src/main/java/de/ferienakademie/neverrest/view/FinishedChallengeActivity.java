package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.ferienakademie.neverrest.R;

public class FinishedChallengeActivity extends Activity implements View.OnClickListener {

    private de.ferienakademie.neverrest.model.Activity mSportiveActivity;
    private Button mCloseButton;
    private Button mShareButton;
    private ImageView mBadgeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_challenge);
        mCloseButton = (Button) findViewById(R.id.buttonClose);
        mShareButton = (Button) findViewById(R.id.buttonShare);
        mBadgeImage = (ImageView) findViewById(R.id.badgeImage);
        String s = mSportiveActivity.getChallenge().getTitle();
        //mBadgeImage.setImageDrawable(R.drawable.badge);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finished_challenge, menu);
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
            case R.id.buttonShare:
                Toast.makeText(this, "NSA already knows about your finished challenge!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonClose:
                Intent intent = new Intent(this,FindChallengesActivity.class);
                startActivity(intent);
                break;
        }
    }
}
