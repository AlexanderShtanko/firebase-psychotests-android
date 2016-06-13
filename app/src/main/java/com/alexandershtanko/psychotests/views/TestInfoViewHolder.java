package com.alexandershtanko.psychotests.views;

import android.content.Context;

import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 13.06.16.
 */
public class TestInfoViewHolder extends AbstractViewHolder {

    public TestInfoViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);

    }

    public static class ViewBinder extends AbstractViewBinder<TestInfoViewHolder,TestInfoViewModel> {


        public ViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {

        }
    }
}
