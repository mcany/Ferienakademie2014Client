package de.ferienakademie.neverrest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import de.ferienakademie.neverrest.controller.DatabaseUtil;

@DatabaseTable(tableName = Activity.TABLE_ACTIVITY)
public final class Activity {

    public final static String TABLE_ACTIVITY = "activity";
    public final static String COL_UUID = "uuid";
    public final static String COL_DURATION = "duration";
    public final static String COL_TIMESTAMP = "timestamp";
    public final static String COL_USER_UUID = "user_uuid";
    public final static String COL_TYPE = "type";


    @DatabaseField(columnName = COL_UUID, id = true)
    private String uuid;

    @DatabaseField(columnName = COL_DURATION)
    private Double duration;

    @DatabaseField(columnName = COL_TIMESTAMP)
    private Long timestamp;

    @DatabaseField(columnName = COL_USER_UUID)
    private String userUuid;

    @DatabaseField(columnName = COL_TYPE)
    private SportsType type;

    public Activity() {
        // ORMLite needs a no-arg constructor
    }


    public Activity(
            String uuid,
            Long timestamp,
            Double duration,
            String userUuid,
            SportsType type) {

        this.uuid = uuid;
        this.timestamp = timestamp;
        this.duration = duration;
        this.userUuid = userUuid;
        this.type = type;

    }


    public String getUuid() {
        return uuid;
    }


    public Double getDuration() {
        return duration;
    }


    public Long getTimestamp() {
        return timestamp;
    }


    public String getUserUuid() {
        return userUuid;
    }


    public SportsType getSportsType() {
        return type;
    }

    //returns the additional lifetime in milliseconds for this activity
    //returns -1 if something unexpected happened
    public double getAdditionalLifetimeInMilliseconds() {
        long additionalLifetimeInMilliseconds = -1;
        try {
            additionalLifetimeInMilliseconds = Energy.gewonneneLebenszeitInMillis(this,DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().queryForId(userUuid));
        } catch (SQLException exception) {
            System.out.println("SQLException");
        }
        return additionalLifetimeInMilliseconds;
    }

    //returns consumped calories for this activity
    //returns -1 if something unexpected happened
    public double getConsumptedEnergyInCalories() {
        double energieverbrauchInKiloCalories = -1.0;
        try {
            energieverbrauchInKiloCalories = Energy.energieverbrauch(this, DatabaseUtil.INSTANCE.getDatabaseHandler().getUserDao().queryForId(userUuid));
        } catch(SQLException exception) {
            System.out.println("SQLException");
        }
        return energieverbrauchInKiloCalories;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setSportsType(SportsType type) {
        this.type = type;
    }

}
