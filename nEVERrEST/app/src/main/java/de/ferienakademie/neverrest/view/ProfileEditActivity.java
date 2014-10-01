package de.ferienakademie.neverrest.view;

import android.app.Activity;
import android.content.Context;
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
public class ProfileEditActivity extends Activity {


    // user
    EditText userNameEdit;
    Spinner userGenderEdit;
    EditText userAgeEdit;
    EditText userSizeEdit;
    EditText userWeightEdit;

    // button
    Button cancelButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // initialize edittexts for user
        userNameEdit = (EditText) findViewById(R.id.userNameEdit);
        userGenderEdit = (Spinner) findViewById(R.id.userGenderEdit);
        userAgeEdit = (EditText) findViewById(R.id.userAgeEdit);
        userSizeEdit = (EditText) findViewById(R.id.userSizeEdit);
        userWeightEdit = (EditText) findViewById(R.id.userWeightEdit);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        // add genders to spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner_gender);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userGenderEdit.setAdapter(adapter);

        userNameEdit.setText("Name");
        userGenderEdit.setSelection(0);
        userAgeEdit.setText("123");
        userSizeEdit.setText("234");
        userWeightEdit.setText("345");

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
            }
        });

    }
}
