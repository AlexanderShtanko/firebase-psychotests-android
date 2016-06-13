package com.alexandershtanko.psychotests.fragments;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestResultViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.views.TestResultViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultFragment extends AbstractFragment<TestResultViewHolder,TestResultViewModel> {

    @Override
    public TestResultViewHolder createViewHolder() {
        return new TestResultViewHolder(getContext(), R.layout.fragment_test_result);
    }

    @Override
    public TestResultViewModel createViewModel() {
        return new TestResultViewModel();
    }

    @Override
    public AbstractViewBinder<TestResultViewHolder, TestResultViewModel> createViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
        return new TestResultViewHolder.ViewBinder(viewHolder,viewModel);
    }
}
