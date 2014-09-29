package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    TextView userSize;
    TextView userWeight;
    TextView userGender;
    TextView userAge;
    // stats
    TextView distanceVertical;
    TextView distanceHorizotal;

    // button
    Button wholeHistoryButton;

    // linear layout for badges
    LinearLayout badgeLayout;

    public LinearLayout createBadge(int image, String text, Context context) {
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_small);

        LinearLayout layout = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding);

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

        // initialize textViews for user
        userName = (TextView) findViewById(R.id.userName);
        userSize = (TextView) findViewById(R.id.userSize);
        userWeight = (TextView) findViewById(R.id.userWeight);
        userGender = (TextView) findViewById(R.id.userGender);
        userAge = (TextView) findViewById(R.id.userAge);
        // ... and stats
        distanceVertical = (TextView) findViewById(R.id.distancesVertical);
        distanceHorizotal = (TextView) findViewById(R.id.distancesHorizontal);
        // linear layout
        badgeLayout = (LinearLayout) findViewById(R.id.lastBadges);
        for (int i = 0; i < 8; i++) {
            badgeLayout.addView(createBadge(R.drawable.sampleimage, "Badge " + i, this));
        }

        // button for older entries
        wholeHistoryButton = (Button) findViewById(R.id.wholeHistoryButton);
        wholeHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Congratulations, you pressed a button.", Toast.LENGTH_SHORT).show();
            }
        });

        // set placeholders for user
        userName.setText(String.format(userName.getText().toString(), "FitFritz"));
        userSize.setText(String.format(userSize.getText().toString(), 180));
        userWeight.setText(String.format(userWeight.getText().toString(), 75));
        userGender.setText(String.format(userGender.getText().toString(), "male"));
        userAge.setText(String.format(userAge.getText().toString(), 42));
        // ... and stats
        distanceVertical.setText(String.format(distanceVertical.getText().toString(), 2, 32));
        distanceHorizotal.setText(String.format(distanceHorizotal.getText().toString(), 250, 200));
    }
}
