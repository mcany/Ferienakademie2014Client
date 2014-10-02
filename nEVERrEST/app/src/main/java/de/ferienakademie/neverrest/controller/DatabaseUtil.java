package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.MetricType;

/**
 * Created by explicat on 9/28/14.
 */
public enum DatabaseUtil {
    INSTANCE, DatabaseUtil;

    private static final String TAG = DatabaseUtil.class.getSimpleName();

    private DatabaseHandler databaseHandler;

    public void initialize(Context context) {
        if (null == databaseHandler) {
            this.databaseHandler = new DatabaseHandler(context);
            // Will create the database if there it has not been created yet
            this.databaseHandler.getWritableDatabase();
        }
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
}