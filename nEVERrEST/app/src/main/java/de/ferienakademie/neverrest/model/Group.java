package de.ferienakademie.neverrest.model;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Group.TABLE_GROUP)
public class Group {

    public static final String TABLE_GROUP = "group";
    public static final String COL_UUID = "uuid";
    public static final String COL_NAME = "name";

    @DatabaseField(columnName = COL_UUID, id = true)
    private String uuid;

    @DatabaseField(columnName = COL_NAME)
    private String name;

    private List<String> userUuids;


    public Group() {
        // ORMLite needs a no-arg constructor
    }


    public Group(
            String uuid,
            String name,
            List<String> userUuids) {

        this.uuid = uuid;
        this.name = name;
        this.userUuids = userUuids;
    }


    public String getUuid() {
        return uuid;
    }


    public String getName() {
        return name;
    }


    public List<String> getUserUuids() {
        return userUuids;
    }

}
