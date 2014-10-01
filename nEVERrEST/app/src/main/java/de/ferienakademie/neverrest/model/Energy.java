package de.ferienakademie.neverrest.model;

/**
 * Created by frederikriedel on 01.10.14.
 */
public class Energy {

    public static double grundumsatz(User user) {
        double mass = user.getMass();
        double height = user.getHeight();
        double age = user.getAge();
        double s=-674.08;
        if(user.getMale()) {
            s = 20.93;
        }
        double grundumsatz = (41.87 * mass + 26.17 * height - 20.93 * age + s);
        return grundumsatz;
    }

    public static double energieverbrauch(double PAL, double duration, double grundumsatz) {
        double energieverbrauch = PAL * duration * (grundumsatz/24.0);
        return energieverbrauch;
    }

    public static double gewonneneLebenszeit() {
        return 42;
    }

}
