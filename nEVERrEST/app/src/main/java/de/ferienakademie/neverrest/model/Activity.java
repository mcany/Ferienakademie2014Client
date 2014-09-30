package de.ferienakademie.neverrest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Activity.TABLE_ACTIVITY)
public final class Activity {

    public final static String TABLE_ACTIVITY = "activity";
    public final static String COL_UUID = "uuid";
    public final static String COL_DURATION = "duration";
    public final static String COL_ENERGY = "energy";
    public final static String COL_ADDITIONAL_LIFETIME = "additional_lifetime";
    public final static String COL_TIMESTAMP = "timestamp";
    public final static String COL_USER_UUID = "user_uuid";
    public final static String COL_TYPE = "type";


    @DatabaseField(columnName = COL_UUID, id = true)
    private String uuid;

    @DatabaseField(columnName = COL_DURATION)
    private Double duration;

    @DatabaseField(columnName = COL_ENERGY)
    private Double energy;

    @DatabaseField(columnName = COL_ADDITIONAL_LIFETIME)
    private Double additionalLiftime;

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
            Double energy,
            Double additionalLiftime,
            String userUuid,
            SportsType type) {

        this.uuid = uuid;
        this.timestamp = timestamp;
        this.duration = duration;
        this.energy=energy;
        this.additionalLiftime=additionalLiftime;
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


    public Double getEnergy() {
        return energy;
    }


    public String getUserUuid() {
        return userUuid;
    }


    public SportsType getSportsType() {
        return type;
    }

    public Double getAdditionalLiftime() {
        return additionalLiftime;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
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