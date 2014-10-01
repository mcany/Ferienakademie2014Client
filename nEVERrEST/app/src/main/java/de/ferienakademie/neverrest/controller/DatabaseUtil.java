package de.ferienakademie.neverrest.controller;

import android.content.Context;

/**
 * Created by explicat on 9/28/14.
 */
public enum DatabaseUtil {
    INSTANCE, DatabaseUtil;

    private DatabaseHandler databaseHandler;

    public void initialize(Context context) {
        if (null == databaseHandler) {
            databaseHandler = new DatabaseHandler(context);
            // Will create the database if there it has not been created yet
            databaseHandler.getWritableDatabase();
        }
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
}
