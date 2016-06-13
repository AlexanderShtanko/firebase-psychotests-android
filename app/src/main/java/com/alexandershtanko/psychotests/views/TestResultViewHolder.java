package com.alexandershtanko.psychotests.views;

import android.content.Context;

import com.alexandershtanko.psychotests.viewmodels.TestResultViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultViewHolder extends AbstractViewHolder {

    public TestResultViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public static class ViewBinder extends AbstractViewBinder<TestResultViewHolder,TestResultViewModel> {

        public ViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {

        }
    }
}
