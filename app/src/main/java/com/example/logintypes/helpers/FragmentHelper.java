package com.example.logintypes.helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentHelper {

    public static final int ADD_FRAGMENT = 1;
    public static final int REPLACE_FRAGMENT = 2;

    public void restartFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.fade_out);
        fragmentTransaction.detach(fragment).attach(fragment).commit();
    }

    public void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.fade_out);
        fragmentTransaction.remove(fragment).commit();
    }

    public void changeFragment(FragmentManager fragmentManager, int container, Fragment fragment,
                               int action, boolean addToBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.fade_out);
        if (action == ADD_FRAGMENT)
            fragmentTransaction.add(container, fragment);
        else if (action == REPLACE_FRAGMENT) {
            fragmentTransaction.replace(container, fragment);
        } else
            return;

        if (addToBackStack)
            fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }
}

