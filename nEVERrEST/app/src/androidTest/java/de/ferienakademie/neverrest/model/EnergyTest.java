package de.ferienakademie.neverrest.model;

import junit.framework.TestCase;

/**
 * Created by frederikriedel on 01.10.14.
 */
public class EnergyTest extends TestCase{

    public void testGrundumsatz() {
        User user = new User("42", "Fritz", "PW", true, 150, 150, 25, 2);
        double grundumsatz = Energy.grundumsatz(user);
        assertEquals(9703.68, grundumsatz, 0.001);
    }

    public void testGewonneneLebenszeitInMillis() {
        User user = new User("42", "Fritz", "PW", true, 150, 150, 25, 2);
        Activity activity = new Activity("42",123456789,10.0,"42",SportsType.RUNNING);
        double gewonneneLebenszeit = Energy.gewonneneLebenszeitInMillis(activity,user);
    }
}