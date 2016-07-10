package com.alexandershtanko.psychotests.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.views.TestInfoViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestInfoFragment extends AbstractFragment<TestInfoViewHolder, TestInfoViewModel> {

    public static final String ARG_TEST_ID = "test_id";

    public static Fragment getInstance(String testId) {
        TestInfoFragment fragment = new TestInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEST_ID,testId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public TestInfoViewHolder createViewHolder() {
        return new TestInfoViewHolder(getContext(), R.layout.fragment_test_info);
    }

    @Override
    public TestInfoViewModel createViewModel() {
        TestInfoViewModel testInfoViewModel = new TestInfoViewModel();

        Bundle args = getArguments();
        String testId = null;
        if (args != null) {
            if (args.containsKey(ARG_TEST_ID)) {
                testId = args.getString(ARG_TEST_ID);
            }
        }

        if (testId != null)
            testInfoViewModel.setTestId(testId);

        return testInfoViewModel;

    }

    @Override
    public AbstractViewBinder<TestInfoViewHolder, TestInfoViewModel> createViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
        return new TestInfoViewHolder.ViewBinder(viewHolder, viewModel);
    }
}

