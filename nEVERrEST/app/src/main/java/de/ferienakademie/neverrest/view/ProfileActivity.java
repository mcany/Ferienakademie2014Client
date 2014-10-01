package de.ferienakademie.neverrest.view;

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
