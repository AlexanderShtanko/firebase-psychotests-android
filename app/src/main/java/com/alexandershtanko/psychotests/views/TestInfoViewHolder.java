package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.utils.Animate;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.utils.Storage;
import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.views.picasso.CircleTransformation;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.start)
    FloatingActionButton startFab;
    @BindView(R.id.show_result)
    Button showResultButton;

    public TestInfoViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);

    }

    public void populate(TestInfo testInfo, boolean hasResult) {
        name.setVisibility(View.GONE);
        category.setVisibility(View.GONE);
        desc.setVisibility(View.GONE);

        name.setText(testInfo.getName());
        category.setText(testInfo.getCategory());
        desc.setText(testInfo.getDesc());

        if (testInfo.getImage() != null && !testInfo.getImage().equals("")) {
            image.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(testInfo.getImage()).transform(new CircleTransformation()).into(image);
        } else {
            image.setVisibility(View.GONE);
        }

        showResultButton.setVisibility(hasResult ? View.VISIBLE : View.GONE);

        Animate.show(name, R.anim.fade_in);
        Animate.show(category, R.anim.fade_in);
        Animate.show(desc, R.anim.fade_in);



    }

    public static class ViewBinder extends AbstractViewBinder<TestInfoViewHolder, TestInfoViewModel> {
        Storage storage;
        public ViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
            super(viewHolder, viewModel);
            storage = new Storage(viewHolder.getContext());
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestInfoObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(info->viewHolder.populate(info,hasResult(info)), ErrorUtils.onError()));

            s.add(RxView.clicks(viewHolder.startFab)
                    .subscribe(v -> ActivityFragments.getInstance().openTest()));
            s.add(RxView.clicks(viewHolder.showResultButton)
                    .subscribe(v-> showResult()));
        }

        private void showResult() {
            ActivityFragments.getInstance().openTestResult(storage.getResult(viewModel.getTestId()));
        }

        private boolean hasResult(TestInfo info) {
            return storage.hasResult(info.getTestId());
        }
    }
}
