package com.alexandershtanko.psychotests.activities;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.ActivityViewModel;
import com.alexandershtanko.psychotests.views.ActivityViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractActivity;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;

public class MainActivity extends AbstractActivity<ActivityViewHolder, ActivityViewModel> {

    private ActivityViewHolder activityViewHolder;

    @Override
    public ActivityViewHolder createViewHolder() {
        activityViewHolder = new ActivityViewHolder(this, R.layout.activity_main);
        return activityViewHolder;
    }

    @Override
    public ActivityViewModel createViewModel() {
        return new ActivityViewModel(this);
    }

    @Override
    public AbstractViewBinder<ActivityViewHolder, ActivityViewModel> createViewBinder(ActivityViewHolder viewHolder, ActivityViewModel viewModel) {
        return new ActivityViewHolder.ViewBinder(viewHolder, viewModel);
    }

    @Override
    public void onBackPressed() {
        if (!activityViewHolder.onBackPressed())
            finish();
    }


    public void updateToolbar(int stringRes)
    {
        activityViewHolder.updateToolbar(stringRes);
    }

    public void updateToolbar(String string) {
        activityViewHolder.updateToolbar(string);

    }
}
