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

import java.sql.SQLException;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.model.User;

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

    // the current user
    User currentUser;

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
                    DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().update(currentUser);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }
        });

    }
}
