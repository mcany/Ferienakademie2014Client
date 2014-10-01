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

    public static long gewonneneLebenszeitInMillis(Activity activity, User user) {
        double grundumsatz = grundumsatz(user);


        double energyConsumption = (grundumsatz/24)*activity.getDuration()*1.4242424242424;

        double met = energyConsumption/(user.getMass())*activity.getDuration();


        double estimatedLifeTime = getLifeTimeForMetPerWeek(user.getEstimatedTrainingSessionsPerWeek()*met);
        long estimatedLifeTimeForThisActivity = Math.ceil(estimatedLifeTime / ((getExpectedLifeTime(user)-user.getAge())*52))*365.249*24*60*60*1000);
        return estimatedLifeTimeForThisActivity;
    }


    private static double getLifeTimeForMetPerWeek(double met) {

        if(met>27.5) {
            return 4.5;
        }
        return 0.04285+0.9743*met-0.1057*Math.pow(met,2) + 0.004967 * Math.pow(met,3)+0.00007995*Math.pow(met,4);
    }

    private static double getExpectedLifeTime(User user) {
        return 42;
    }
}
