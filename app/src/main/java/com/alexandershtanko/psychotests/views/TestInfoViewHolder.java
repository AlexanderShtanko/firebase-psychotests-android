package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 13.06.16.
 */
public class TestInfoViewHolder extends AbstractViewHolder {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.start)
    FloatingActionButton startFab;

    public TestInfoViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);

    }

    public void populate(TestInfo testInfo) {
        name.setText(testInfo.getName());
        category.setText(testInfo.getCategory());
        desc.setText(testInfo.getDesc());
    }

    public static class ViewBinder extends AbstractViewBinder<TestInfoViewHolder, TestInfoViewModel> {

        public ViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestInfoObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(viewHolder::populate));

            s.add(RxView.clicks(viewHolder.startFab)
                    .subscribe(v -> ActivityFragments.getInstance().openTest()));
        }
    }
}
