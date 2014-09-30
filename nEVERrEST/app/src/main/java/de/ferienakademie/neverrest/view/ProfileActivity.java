package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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

import de.ferienakademie.neverrest.R;

/**
 * Created by arno on 29/09/14.
 */
public class ProfileActivity extends Activity {

    // user
    EditText userName;
    EditText userSize;
    EditText userWeight;
    Spinner userGender;
    EditText userAge;
    // stats
    TextView distanceVertical;
    TextView distanceHorizotal;

    // buttons
    Button enableEditingButton;
    Button wholeHistoryButton;

    // edit-mode
    boolean isInEditMode = false;

    // linear layout for badges
    LinearLayout badgeLayout;

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

    public void enableEditing(EditText edit) {
        edit.setBackground(new EditText(this).getBackground());
        edit.setClickable(true);
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
    }

    public void finishEditing(EditText edit) {
        edit.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        edit.setClickable(false);
        edit.setFocusable(false);
        edit.setFocusableInTouchMode(false);
    }

    public void enableEditingGlobal() {
        enableEditing(userName);
        enableEditing(userSize);
        enableEditing(userWeight);
        enableEditing(userAge);
        userGender.setEnabled(true);
        userGender.setClickable(true);
        userGender.setBackground(new Spinner(this).getBackground());
    }

    public void finishEditingGlobal() {
        finishEditing(userName);
        finishEditing(userSize);
        finishEditing(userWeight);
        finishEditing(userAge);
        userGender.setEnabled(false);
        userGender.setClickable(false);
        userGender.setBackgroundColor(Color.parseColor("#00FFFFFF"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initialize textViews for user
        userName = (EditText) findViewById(R.id.userName);
        userSize = (EditText) findViewById(R.id.userSize);
        userWeight = (EditText) findViewById(R.id.userWeight);
        userGender = (Spinner) findViewById(R.id.userGender);
        userAge = (EditText) findViewById(R.id.userAge);
        // ... and stats
        distanceVertical = (TextView) findViewById(R.id.distancesVertical);
        distanceHorizotal = (TextView) findViewById(R.id.distancesHorizontal);
        // linear layout
        badgeLayout = (LinearLayout) findViewById(R.id.lastBadges);
        for (int i = 0; i < 8; i++) {
            badgeLayout.addView(createBadge(R.drawable.sampleimage, "Badge " + i, this));
        }

        // add genders to spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner_gender);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userGender.setAdapter(adapter);

        // button for older entries
        wholeHistoryButton = (Button) findViewById(R.id.wholeHistoryButton);
        wholeHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Congratulations, you pressed a button.", Toast.LENGTH_SHORT).show();
            }
        });

        enableEditingButton = (Button) findViewById(R.id.enableEditingButton);
        enableEditingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInEditMode) {
                    finishEditingGlobal();
                    isInEditMode = false;
                    enableEditingButton.setText("Edit Profile");
                } else {
                    enableEditingGlobal();
                    isInEditMode = true;
                    enableEditingButton.setText("Finish Editing");
                }
            }
        });

        // set placeholders for user
        userName.setText(String.format(userName.getText().toString(), "FitFritz"));
        userSize.setText(String.format(userSize.getText().toString(), 180));
        userWeight.setText(String.format(userWeight.getText().toString(), 75));
        userGender.setSelection(0);
        userAge.setText(String.format(userAge.getText().toString(), 42));
        // ... and stats
        distanceVertical.setText(String.format(distanceVertical.getText().toString(), 2, 32));
        distanceHorizotal.setText(String.format(distanceHorizotal.getText().toString(), 250, 200));

        // disable all buttons per default
        finishEditingGlobal();
    }
}
