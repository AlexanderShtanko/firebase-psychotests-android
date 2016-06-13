package com.alexandershtanko.psychotests.fragments;

import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestsViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.views.TestsViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsFragment extends AbstractFragment<TestsViewHolder,TestsViewModel> {

    @Override
    public TestsViewHolder createViewHolder() {
        return new TestsViewHolder(getContext(), R.layout.fragment_tests);
    }

    @Override
    public TestsViewModel createViewModel() {
        return new TestsViewModel();
    }

    @Override
    public AbstractViewBinder<TestsViewHolder, TestsViewModel> createViewBinder(TestsViewHolder viewHolder, TestsViewModel viewModel) {
        return new TestsViewHolder.ViewBinder(viewHolder,viewModel);
    }

    public static Fragment getInstance() {
        TestsFragment fragment = new TestsFragment();
        return fragment;
    }
}
