package ferienakademie.de.fitfritz.model;

import java.util.Date;

/**
 * Created by explicat on 9/25/14.
 */
public abstract class SensorData {

    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
