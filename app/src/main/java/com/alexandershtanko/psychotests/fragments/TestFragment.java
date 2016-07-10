package com.alexandershtanko.psychotests.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestViewModel;
import com.alexandershtanko.psychotests.views.TestViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestFragment extends AbstractFragment<TestViewHolder,TestViewModel> {


    private static final String ARG_TEST_ID = "test_id";

    @Override
    public TestViewHolder createViewHolder() {
        return new TestViewHolder(getContext(), R.layout.fragment_test);
    }

    @Override
    public TestViewModel createViewModel() {
        TestViewModel testViewModel = new TestViewModel();
        Bundle args = getArguments();
        String testId = null;
        if (args != null) {
            if (args.containsKey(ARG_TEST_ID)) {
                testId = args.getString(ARG_TEST_ID);
            }
        }

        if (testId != null)
            testViewModel.setTestId(testId);

        return testViewModel;
    }

    @Override
    public AbstractViewBinder<TestViewHolder, TestViewModel> createViewBinder(TestViewHolder viewHolder, TestViewModel viewModel) {
        return new TestViewHolder.ViewBinder(viewHolder,viewModel);
    }

    public static Fragment getInstance(String testId) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEST_ID,testId);
        fragment.setArguments(args);
        return fragment;
    }
}
