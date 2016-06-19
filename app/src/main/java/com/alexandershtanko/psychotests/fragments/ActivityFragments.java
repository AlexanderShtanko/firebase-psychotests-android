package com.alexandershtanko.psychotests.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by aleksandr on 13.06.16.
 */
public class ActivityFragments {

    private static ActivityFragments instance;
    private FragmentManager fragmentManager;
    private int containerId;

    public static synchronized ActivityFragments getInstance() {
        if (instance == null)
            instance = new ActivityFragments();
        return instance;
    }

    public void init(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }


    public void openTests() {
        replaceFragment(TestsFragment.getInstance());
    }

    public void openCategories() {
        replaceFragment(CategoriesFragment.getInstance());
    }

    public void openTestsDone() {
        replaceFragment(TestsFragment.getInstance());
    }

    public void openTestInfo() {
        replaceFragment(TestInfoFragment.getInstance());
    }
    public void openSettings() {
        replaceFragment(TestsFragment.getInstance());
    }

    public void openTest() {
        replaceFragment(TestFragment.getInstance());
    }

    public void openTestResult() {
        replaceFragment(TestResultFragment.getInstance());
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    public void destroy() {
        fragmentManager = null;
    }



}
