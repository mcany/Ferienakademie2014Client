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
    public static final String COL_ESTIMATED_TRAINING_SESSIONS_PER_WEEK= "estimatedTrainingSessionsPerWeek";

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

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

    @DatabaseField(columnName = COL_ESTIMATED_TRAINING_SESSIONS_PER_WEEK)
    private int estimatedTrainingSessionsPerWeek;


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
            double age,
            int estimatedTrainingSessionsPerWeek) {

        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.male = male;
        this.mass = mass;
        this.height = height;
        this.age = age;
        this.estimatedTrainingSessionsPerWeek=estimatedTrainingSessionsPerWeek;

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

    public int getEstimatedTrainingSessionsPerWeek() {
        return estimatedTrainingSessionsPerWeek;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public void setEstimatedTrainingSessionsPerWeek(int estimatedTrainingSessionsPerWeek) {
        this.estimatedTrainingSessionsPerWeek = estimatedTrainingSessionsPerWeek;
    }

    @Override
    public String toString() {
        return username + " (" + uuid + ")";
    }

}
