package com.alexandershtanko.psychotests.fragments;

import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.SessionManager;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.views.TestInfoViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestInfoFragment extends AbstractFragment<TestInfoViewHolder, TestInfoViewModel> {

    public static Fragment getInstance() {
        TestInfoFragment fragment = new TestInfoFragment();
        return fragment;
    }

    @Override
    public TestInfoViewHolder createViewHolder() {
        return new TestInfoViewHolder(getContext(), R.layout.fragment_test_info);
    }

    @Override
    public TestInfoViewModel createViewModel() {
        TestInfoViewModel testInfoViewModel = new TestInfoViewModel();
        Test test = SessionManager.getInstance().getTest();
        if (test != null)
            testInfoViewModel.setTestInfo(test.getInfo());
        return testInfoViewModel;

    }

    @Override
    public AbstractViewBinder<TestInfoViewHolder, TestInfoViewModel> createViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
        return new TestInfoViewHolder.ViewBinder(viewHolder, viewModel);
    }
}

