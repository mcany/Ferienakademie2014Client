package de.ferienakademie.neverrest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

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
    public static final String COL_CONTINENT_NAME = "continentName";

    @DatabaseField(columnName = COL_UUID, id = true)
    private String uuid;

    @DatabaseField(columnName = COL_CONTINENT_NAME)
    private String continentName;

    @DatabaseField(columnName = COL_TITLE)
    private String title;

    @DatabaseField(columnName = COL_TYPE)
    private MetricType type;

    @DatabaseField(columnName = COL_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COL_ICON)
	private int iconResourceId;

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
        this.uuid = UUID.randomUUID().toString();
    }


    public Challenge(
            String uuid,
            String title,
            MetricType type,
            String description,
			int iconResourceId,
            double totalEffort,
            double completedEffort,
            long timestampStarted,
            long timestampLastModified,
            boolean finished,
            String continentName,
            double lat,
            double lon) {

        this.uuid = uuid;
        this.title = title;
        this.type = type;
        this.description = description;
		this.iconResourceId = iconResourceId;
        this.totalEffort = totalEffort;
        this.completedEffort = completedEffort;
        this.timestampStarted = timestampStarted;
        this.timestampLastModified = timestampLastModified;
        this.finished = finished;
        this.continentName = continentName;
        this.startingLatitude = lat;
        this.startingLongitude = lon;
    }


    public String getUuid() {
        return uuid;
    }


    public String getTitle() {
        return title;
    }


    public MetricType getType() {
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

    public void setType(MetricType type) {
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

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public void setTimestampLastModified(long timestampLastModified) {
        this.timestampLastModified = timestampLastModified;

    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

	public int getIconResourceId() {
		return iconResourceId;
	}

	public void setIconResourceId(int iconResourceId) {
		this.iconResourceId = iconResourceId;
	}


    public static class Builder {

        private String uuid, title, description, continentName;
		private int iconResourceId;
        private MetricType type;
        private double totalEffort, completedEffort, lat, lon;
        private long timestampStarted, timestampLastModified;
        private boolean finished;


        public Builder() {
            this.uuid = UUID.randomUUID().toString();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }


        public Builder description(String description) {
            this.description = description;
            return this;
        }


        public Builder iconResourceId(int iconResourceId) {
			this.iconResourceId = iconResourceId;
            return this;
        }


        public Builder continentName(String continentName) {
            this.continentName = continentName;
            return this;
        }


        public Builder metricType(MetricType type) {
            this.type = type;
            return this;
        }


        public Builder totalEffort(double totalEffort) {
            this.totalEffort = totalEffort;
            return this;
        }


        public Builder completedEffort(double completedEffort) {
            this.completedEffort = completedEffort;
            return this;
        }


        public Builder timestampStarted(long timestampStarted) {
            this.timestampStarted = timestampStarted;
            return this;
        }


        public Builder timestampLastModified(long timestampLastModified) {
            this.timestampLastModified = timestampLastModified;
            return this;
        }


        public Builder finished(boolean finished) {
            this.finished = finished;
            return this;
        }


        public Builder lat(double lat) {
            this.lat = lat;
            return this;
        }


        public Builder lon(double lon) {
            this.lon = lon;
            return this;
        }


        public Challenge build() {
            return new Challenge(
                    uuid,
                    title,
                    type,
                    description,
					iconResourceId,
                    totalEffort,
                    completedEffort,
                    timestampStarted,
                    timestampLastModified,
                    finished,
                    continentName,
                    lat,
                    lon);
        }
    }
}
