package de.ferienakademie.neverrest.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by explicat on 9/25/14.
 */
public abstract class SensorData {

    public static final String COL_CREATION_DATE = "creationDate";
    public static final String COL_ACTIVITY = "activity";

    @DatabaseField(columnName = COL_CREATION_DATE)
    protected Date creationDate;

    @DatabaseField(columnName = COL_ACTIVITY, foreign = true)
    protected Activity mActivity;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Activity getActivity() { return mActivity; }
}
