package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.viewmodels.TestResultViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultViewHolder extends AbstractViewHolder {
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.button_done)
    FloatingActionButton doneFab;

    public TestResultViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public void populateResult(TestResult result) {
        text.setText(result.getText());
    }

    public void populateTestInfo(TestInfo testInfo) {
        name.setText(testInfo.getName());
    }

    public static class ViewBinder extends AbstractViewBinder<TestResultViewHolder, TestResultViewModel> {

        public ViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
            super(viewHolder, viewModel);
        }


        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestResultObservable().subscribeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateResult));
            s.add(viewModel.getTestInfoObservable().subscribeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateTestInfo));
            //s.add(RxView.clicks(viewHolder.doneFab).subscribe(v-> ActivityFragments.getInstance().openTests()));
        }
    }
}
