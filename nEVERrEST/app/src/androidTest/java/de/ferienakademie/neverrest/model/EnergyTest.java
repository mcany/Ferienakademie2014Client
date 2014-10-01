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
}
