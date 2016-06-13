package com.alexandershtanko.psychotests.activities;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.ActivityViewModel;
import com.alexandershtanko.psychotests.views.ActivityViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractActivity;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

public class MainActivity extends AbstractActivity<ActivityViewHolder,ActivityViewModel> {

    @Override
    public ActivityViewHolder createViewHolder() {
        return new ActivityViewHolder(this, R.layout.activity_main);
    }

    @Override
    public ActivityViewModel createViewModel() {
        return new ActivityViewModel();
    }

    @Override
    public AbstractViewBinder<ActivityViewHolder, ActivityViewModel> createViewBinder(ActivityViewHolder viewHolder, ActivityViewModel viewModel) {
        return new ActivityViewHolder.ViewBinder(viewHolder,viewModel);
    }
}
