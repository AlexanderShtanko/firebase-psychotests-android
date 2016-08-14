package com.alexandershtanko.psychotests.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.activities.MainActivity;
import com.alexandershtanko.psychotests.viewmodels.TestsViewModel;
import com.alexandershtanko.psychotests.views.TestsViewHolder;
import com.alexandershtanko.psychotests.views.adapters.SortedCallback;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsFragment extends AbstractFragment<TestsViewHolder, TestsViewModel> {

    public static final String ARG_CATEGORY = "category";
    public static final String ARG_PASSED = "passed";
    private static final String ARG_FAVORITE = "favorite";

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

    public static Fragment getInstanceForFavoriteTests() {
        TestsFragment fragment = new TestsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FAVORITE, true);
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
        Boolean onlyFavorite = false;

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_CATEGORY)) {
                category = args.getString(ARG_CATEGORY);
            }
            if (args.containsKey(ARG_PASSED)) {
                onlyPassed = args.getBoolean(ARG_PASSED);
            }

            if (args.containsKey(ARG_FAVORITE)) {
                onlyFavorite = args.getBoolean(ARG_FAVORITE);
            }
        }

        testsViewModel.setFilter(category, onlyPassed, onlyFavorite);
        return testsViewModel;
    }

    @Override
    public AbstractViewBinder<TestsViewHolder, TestsViewModel> createViewBinder(TestsViewHolder viewHolder, TestsViewModel viewModel) {
        return new TestsViewHolder.ViewBinder(getMainActivity(), viewHolder, viewModel);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tests_appbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                PopupMenu popupMenu = new PopupMenu(getMainActivity(), ((MainActivity) getMainActivity()).getViewHolder().getToolbar(), Gravity.RIGHT | Gravity.TOP);

                popupMenu.getMenuInflater().inflate(R.menu.tests_sort,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item1 -> {
                    switch (item1.getItemId()) {
                        case R.id.action_date:
                            getViewModel().setSortType(SortedCallback.SortType.Date);
                            break;
                        case R.id.action_popularity:
                            getViewModel().setSortType(SortedCallback.SortType.Popularity);
                            break;

                    }
                    return true;
                });

                popupMenu.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
