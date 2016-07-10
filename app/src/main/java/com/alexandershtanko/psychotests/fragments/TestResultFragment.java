package com.alexandershtanko.psychotests.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestResultViewModel;
import com.alexandershtanko.psychotests.views.TestResultViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultFragment extends AbstractFragment<TestResultViewHolder, TestResultViewModel> {

    public static final String ARG_TEST_ID = "test_id";

    public static Fragment getInstance(String testId) {
        TestResultFragment fragment = new TestResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEST_ID,testId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public TestResultViewHolder createViewHolder() {
        return new TestResultViewHolder(getContext(), R.layout.fragment_test_result);
    }

    @Override
    public TestResultViewModel createViewModel() {
        TestResultViewModel testResultViewModel = new TestResultViewModel();

        Bundle args = getArguments();
        String testId = null;
        if (args != null) {
            if (args.containsKey(ARG_TEST_ID)) {
                testId = args.getString(ARG_TEST_ID);
            }
        }

        if (testId != null)
            testResultViewModel.setTestId(testId);

        return testResultViewModel;
    }

    @Override
    public AbstractViewBinder<TestResultViewHolder, TestResultViewModel> createViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
        return new TestResultViewHolder.ViewBinder(viewHolder, viewModel);
    }
}
