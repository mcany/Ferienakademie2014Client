package de.ferienakademie.neverrest.model;

import com.j256.ormlite.table.DatabaseTable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.j256.ormlite.field.DatabaseField;


import java.io.Serializable;

/**
 * Created by explicat on 9/28/14.
 */


@DatabaseTable(tableName = Challenge.TABLE_CHALLENGE)
public class Challenge implements Serializable {

    public static final String TABLE_CHALLENGE = "challenge";
    public static final String COL_UUID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_TYPE = "type";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_ICON = "icon";
    public static final String COL_STARTING_LATITUDE = "latitude";
    public static final String COL_STARTING_LONGITUDE = "longitude";
    public static final String COL_EFFORT_TOTAL = "effort_total";
    public static final String COL_EFFORT_COMPLETED = "effort_completed";
    public static final String COL_TIMESTAMP_STARTED = "started";
    public static final String COL_TIMESTAMP_LAST_MODIFIED = "last_modified";
    public static final String COL_FINISHED = "finished";

    @DatabaseField(columnName = COL_UUID, id = true)
    private String uuid;

    @DatabaseField(columnName = COL_TITLE)
    private String title;

    @DatabaseField(columnName = COL_TYPE)
    private SportsType type;

    @DatabaseField(columnName = COL_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COL_ICON)
    private String iconPath;

    @DatabaseField(columnName = COL_STARTING_LATITUDE)
    private double startingLatitude;

    @DatabaseField(columnName = COL_STARTING_LONGITUDE)
    private double startingLongitude;

    @DatabaseField(columnName = COL_EFFORT_TOTAL)
    private double totalEffort;

    @DatabaseField(columnName = COL_EFFORT_COMPLETED)
    private double completedEffort;

    @DatabaseField(columnName = COL_TIMESTAMP_STARTED)
    private long timestampStarted;

    @DatabaseField(columnName = COL_TIMESTAMP_LAST_MODIFIED)
    private long timestampLastModified;

    @DatabaseField(columnName = COL_FINISHED)
    private boolean finished;


    public Challenge() {
        // ORMLite needs a no-arg constructor
    }


    public Challenge(
            String uuid,
            String title,
            SportsType type,
            String description,
            String iconPath,
            double totalEffort,
            double completedEffort,
            long timestampStarted,
            long timestampLastModified,
            boolean finished) {

        this.uuid = uuid;
        this.title = title;
        this.type = type;
        this.description = description;
        this.iconPath = iconPath;
        this.totalEffort = totalEffort;
        this.completedEffort = completedEffort;
        this.timestampStarted = timestampStarted;
        this.timestampLastModified = timestampLastModified;
        this.finished = finished;
    }


    public String getUuid() {
        return uuid;
    }


    public String getTitle() {
        return title;
    }


    public SportsType getType() {
        return type;
    }


    public String getDescription() {
        return description;
    }

    public double getTotalEffort() {
        return totalEffort;
    }

    public double getCompletedEffort() {
        return completedEffort;
    }

    public long getTimestampStarted() {
        return timestampStarted;
    }

    public long getTimestampLastModified() {
        return timestampLastModified;
    }

    public boolean isFinished() {
        return finished;
    }

    public double getStartingLatitude() {
        return startingLatitude;
    }

    public double getStartingLongitude() {
        return startingLongitude;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(SportsType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartingLatitude(double startingLatitude) {
        this.startingLatitude = startingLatitude;
    }

    public void setStartingLongitude(double startingLongitude) {
        this.startingLongitude = startingLongitude;
    }

    public void setTotalEffort(double totalEffort) {
        this.totalEffort = totalEffort;
    }

    public void setCompletedEffort(double completedEffort) {
        this.completedEffort = completedEffort;
    }

    public void setTimestampStarted(long timestampStarted) {
        this.timestampStarted = timestampStarted;
    }

    public void setTimestampLastModified(long timestampLastModified) {
        this.timestampLastModified = timestampLastModified;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getIconPath() {
        return iconPath;
    }

    public Bitmap getIconAsBitmap() {
        return BitmapFactory.decodeFile(iconPath);
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
