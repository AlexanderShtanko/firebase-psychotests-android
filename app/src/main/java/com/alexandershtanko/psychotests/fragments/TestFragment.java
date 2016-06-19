package com.alexandershtanko.psychotests.fragments;

import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.SessionManager;
import com.alexandershtanko.psychotests.viewmodels.TestViewModel;
import com.alexandershtanko.psychotests.views.TestViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestFragment extends AbstractFragment<TestViewHolder,TestViewModel> {


    @Override
    public TestViewHolder createViewHolder() {
        return new TestViewHolder(getContext(), R.layout.fragment_test);
    }

    @Override
    public TestViewModel createViewModel() {
        TestViewModel testViewModel = new TestViewModel();
        testViewModel.setTest(SessionManager.getInstance().getTest());
        return testViewModel;
    }

    @Override
    public AbstractViewBinder<TestViewHolder, TestViewModel> createViewBinder(TestViewHolder viewHolder, TestViewModel viewModel) {
        return new TestViewHolder.ViewBinder(viewHolder,viewModel);
    }

    public static Fragment getInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }
}
