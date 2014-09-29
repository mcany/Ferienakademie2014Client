package de.ferienakademie.neverrest.model;

/**
 * Created by arno on 29/09/14.
 */
public class Challenge {

    private String name;
    private String details;
    private int imageName;

    public Challenge(String name, String details, int imageName) {
        this.name = name;
        this.details = details;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getImageName() {
        return imageName;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }
}
