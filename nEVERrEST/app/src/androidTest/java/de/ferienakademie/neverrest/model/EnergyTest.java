package de.ferienakademie.neverrest.model;

import android.util.Log;

import junit.framework.TestCase;
import java.lang.reflect.Method;
import java.lang.reflect.*;

/**
 * Created by frederikriedel on 01.10.14.
 */
public class EnergyTest extends TestCase{

    public void testGrundumsatz() {
        User user = new User("42", "Fritz", "PW", true, 150, 150, 25, 2);
        double grundumsatz=0;

        //test private methods
        try {
            Method m = Class.forName("de.ferienakademie.neverrest.model.Energy").getDeclaredMethod("grundumsatz", User.class);
            m.setAccessible(true);
            grundumsatz = ((Double)m.invoke(null, user)).doubleValue();
        } catch (Exception e) {
            Log.e("Error",e+"");
        }
        assertEquals(9703.68, grundumsatz, 0.001);
    }

    public void testKiloJouleToCalories() {
        double result = 0;
        //test private methods
        try {
            Method m = Class.forName("de.ferienakademie.neverrest.model.Energy").getDeclaredMethod("kiloJouleToCalories", User.class);
            m.setAccessible(true);
            result = ((Double)m.invoke(null, 1.0)).doubleValue();
        } catch (Exception e) {
            Log.e("Error",e+"");
        }
        assertEquals(239.005736, result, 0.001);
    }

}