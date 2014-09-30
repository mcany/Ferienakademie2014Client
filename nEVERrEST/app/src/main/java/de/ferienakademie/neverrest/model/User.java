package de.ferienakademie.neverrest.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = User.TABLE_USER)
public final class User {

    public static final String TABLE_USER = "user";
    public static final String COL_UUID = "uuid";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_MALE = "male";
    public static final String COL_MASS = "mass";
    public static final String COL_HEIGHT = "height";
    public static final String COL_AGE = "age";

    @DatabaseField(columnName = COL_UUID)
    private String uuid;

    @DatabaseField(columnName = COL_USERNAME)
    private String username;

    @DatabaseField(columnName = COL_PASSWORD)
    private String password;

    @DatabaseField(columnName = COL_MALE)
    private boolean male;

    @DatabaseField(columnName = COL_MASS)
    private double mass;

    @DatabaseField(columnName = COL_HEIGHT)
    private double height;

    @DatabaseField(columnName = COL_AGE)
    private double age;


    public User() {
        // ORMLite needs a no-arg constructor
    }

    public User(
            String uuid,
            String username,
            String password,
            boolean male,
            double mass,
            double height,
            double age) {

        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.male = male;
        this.mass = mass;
        this.height = height;
        this.age = age;

    }


    public String getUuid() {
        return uuid;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public double getAge() {
        return age;
    }


    public boolean getMale() {
        return male;
    }


    public double getMass() {
        return mass;
    }


    public double getHeight() {
        return height;
    }


    @Override
    public String toString() {
        return username + " (" + uuid + ")";
    }

}
