package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.utils.Animate;
import com.alexandershtanko.psychotests.viewmodels.TestResultViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;
import com.jakewharton.rxbinding.view.RxView;

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
    @BindView(R.id.repeat)
    Button repeatButton;

    public TestResultViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public void populateResult(TestResult result) {
        text.setVisibility(View.GONE);
        if (result != null)
            text.setText(result.getText());
        else
            text.setText("Результат не найден");

        Animate.show(text, R.anim.expand_from_bottom);


    }

    public void populateTestInfo(TestInfo testInfo) {
        name.setVisibility(View.GONE);
        name.setText(testInfo.getName());
        Animate.show(name, R.anim.expand_from_top);


    }

    public static class ViewBinder extends AbstractViewBinder<TestResultViewHolder, TestResultViewModel> {

        public ViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
            super(viewHolder, viewModel);
        }


        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestResultObservable().subscribeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateResult));
            s.add(viewModel.getTestInfoObservable().subscribeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateTestInfo));
            s.add(RxView.clicks(viewHolder.doneFab).subscribe(v -> ActivityFragments.getInstance().openTests()));
            s.add(RxView.clicks(viewHolder.repeatButton).subscribe(v -> ActivityFragments.getInstance().openTest()));
        }
    }
}
