package com.alexandershtanko.psychotests.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestsViewModel;
import com.alexandershtanko.psychotests.views.TestsViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsFragment extends AbstractFragment<TestsViewHolder, TestsViewModel> {

    public static final String ARG_CATEGORY = "category";
    public static final String ARG_PASSED = "passed";

    public static Fragment getInstance() {
        return new TestsFragment();
    }

    public static Fragment getInstance(String category) {
        TestsFragment fragment = new TestsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);

        return fragment;
    }

    public static Fragment getInstanceForPassedTests() {
        TestsFragment fragment = new TestsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PASSED, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public TestsViewHolder createViewHolder() {
        return new TestsViewHolder(getContext(), R.layout.fragment_tests);
    }

    @Override
    public TestsViewModel createViewModel() {
        TestsViewModel testsViewModel = new TestsViewModel(getContext());

        String category = null;
        Boolean onlyPassed = false;

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_CATEGORY)) {
                category = args.getString(ARG_CATEGORY);
            }
            if (args.containsKey(ARG_PASSED)) {
                onlyPassed = args.getBoolean(ARG_PASSED);
            }
        }

        testsViewModel.setFilter(category,onlyPassed);

        return testsViewModel;
    }

    @Override
    public AbstractViewBinder<TestsViewHolder, TestsViewModel> createViewBinder(TestsViewHolder viewHolder, TestsViewModel viewModel) {
        return new TestsViewHolder.ViewBinder(getMainActivity(),viewHolder, viewModel);
    }
}
