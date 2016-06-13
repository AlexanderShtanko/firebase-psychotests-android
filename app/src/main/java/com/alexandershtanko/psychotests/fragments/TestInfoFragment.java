package com.alexandershtanko.psychotests.fragments;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.views.TestInfoViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestInfoFragment extends AbstractFragment<TestInfoViewHolder,TestInfoViewModel> {


    @Override
    public TestInfoViewHolder createViewHolder() {
        return new TestInfoViewHolder(getContext(), R.layout.fragment_test_info);
    }

    @Override
    public TestInfoViewModel createViewModel() {
        return new TestInfoViewModel();
    }

    @Override
    public AbstractViewBinder<TestInfoViewHolder, TestInfoViewModel> createViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
        return new TestInfoViewHolder.ViewBinder(viewHolder,viewModel);
    }
}
