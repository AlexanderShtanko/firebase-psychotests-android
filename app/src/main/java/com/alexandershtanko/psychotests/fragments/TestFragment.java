package com.alexandershtanko.psychotests.fragments;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.views.TestViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;

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
        return new TestViewModel();
    }

    @Override
    public AbstractViewBinder<TestViewHolder, TestViewModel> createViewBinder(TestViewHolder viewHolder, TestViewModel viewModel) {
        return new TestViewHolder.ViewBinder(viewHolder,viewModel);
    }
}
