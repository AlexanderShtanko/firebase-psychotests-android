package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.helpers.RateUsHelper;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.utils.Animate;
import com.alexandershtanko.psychotests.utils.StringUtils;
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
    @BindView(R.id.share_result)
    View shareResultButton;
    @BindView(R.id.like)
    FloatingActionButton like;
    @BindView(R.id.dislike)
    FloatingActionButton dislike;

    public TestResultViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
        doneFab.hide();
        doneFab.show();
    }

    public void populateResult(TestResult result) {
        text.setVisibility(View.GONE);
        if (result != null)
            text.setText(result.getText());
        else
            text.setText(R.string.error_result_not_found);

        Animate.show(text, R.anim.fade_in);


    }

    public void populateTestInfo(TestInfo testInfo) {
        name.setVisibility(View.GONE);
        name.setText(StringUtils.capitalizeSentences(testInfo.getName()));
        Animate.show(name, R.anim.fade_in);


    }

    public void populateLikeStatus(Boolean likeStatus) {
        if (likeStatus == null) {
            dislike.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));
            like.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));

        } else if (likeStatus) {
            like.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.orange)));
            dislike.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));
        } else {
            dislike.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.orange)));
            like.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));
        }

    }

    private void shareResult(TestInfo testInfo, TestResult testResult) {
        if (testInfo != null && testResult != null)
            RateUsHelper.shareTheResult(getContext(), testInfo.getName(), testResult.getText());
    }

    public static class ViewBinder extends AbstractViewBinder<TestResultViewHolder, TestResultViewModel> {

        public ViewBinder(TestResultViewHolder viewHolder, TestResultViewModel viewModel) {
            super(viewHolder, viewModel);
        }


        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestResultObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateResult));
            s.add(viewModel.getTestInfoObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateTestInfo));
            s.add(viewModel.getLikeStatusObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateLikeStatus));

            s.add(RxView.clicks(viewHolder.doneFab).subscribe(v -> done()));
            s.add(RxView.clicks(viewHolder.repeatButton).subscribe(v -> ActivityFragments.getInstance().openTest(viewModel.getTestId())));

            s.add(RxView.clicks(viewHolder.shareResultButton).subscribe(v -> viewHolder.shareResult(viewModel.getTestInfo(), viewModel.getTestResult())));
            s.add(RxView.clicks(viewHolder.like).subscribe(v -> like()));
            s.add(RxView.clicks(viewHolder.dislike).subscribe(v -> dislike()));


        }

        private void dislike() {
            viewModel.dislike();
        }

        private void like() {
            viewModel.like();
        }

        private void done() {
            if (RateUsHelper.needShowRateUs(viewHolder.getContext()))
                RateUsHelper.doYouLikeApplication(viewHolder.getContext());
            ActivityFragments.getInstance().openTests();
        }
    }
}
