package de.ferienakademie.neverrest.view;

import static de.ferienakademie.neverrest.view.NavigationDrawerFragment.NavigationDrawerCallbacks;

/**
 * Created by Robert on 29.09.2014.
 */
public interface NeverrestInterface extends NavigationDrawerCallbacks {

    public abstract void setUpNavigationDrawer();

    public abstract void onNavigationDrawerItemSelected(int position);

    public abstract void restoreActionBar();


}
