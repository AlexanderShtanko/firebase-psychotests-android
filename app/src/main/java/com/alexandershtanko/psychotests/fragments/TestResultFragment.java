package com.alexandershtanko.psychotests.fragments;

import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.SessionManager;
import com.alexandershtanko.psychotests.viewmodels.TestResultViewModel;
import com.alexandershtanko.psychotests.views.TestResultViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultFragment extends AbstractFragment<TestResultViewHolder, TestResultViewModel> {

    public static Fragment getInstance() {
        TestResultFragment fragment = new TestResultFragment();
        return fragment;
    }

    @Override
    public TestResultViewHolder createViewHolder() {
        return new TestResultViewHolder(getContext(), R.layout.fragment_test_result);
    }

    @Override
    public TestResultViewModel createViewModel() {
        TestResultViewModel testResultViewModel = new TestResultViewModel();
        testResultViewModel.setTest(SessionManager.getInstance().getTest());
        testResultViewModel.setResultValue(SessionManager.getInstance().getResult());
        return testResultViewModel;
    }

    @Override
    public AbstractViewBinder<TestResultViewHolder, TestResultViewModel> createViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
        return new TestResultViewHolder.ViewBinder(viewHolder, viewModel);
    }
}
