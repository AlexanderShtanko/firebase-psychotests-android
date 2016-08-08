package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.utils.Animate;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.utils.StringUtils;
import com.alexandershtanko.psychotests.viewmodels.TestInfoViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
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
        startFab.setVisibility(View.GONE);
        Animate.show(startFab,R.anim.scale_in);



    }

    public void populateTestInfo(TestInfo testInfo) {
        name.setText(StringUtils.capitalizeSentences(testInfo.getName()));
        category.setText(StringUtils.capitalizeSentences(testInfo.getCategory()));
        desc.setText(testInfo.getDesc());

        if (testInfo.getImage() != null && !testInfo.getImage().equals("")) {
            image.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(testInfo.getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).bitmapTransform(new CropCircleTransformation(getContext())).into(image);
            Glide.with(getContext()).load(testInfo.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Palette.from(resource).generate(palette -> {

                    });
                }
            });
        } else {
            image.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.tree).bitmapTransform(new CropCircleTransformation(getContext())).into(image);

        }


        Animate.show(name, R.anim.fade_in);
        Animate.show(category, R.anim.fade_in);
        Animate.show(desc, R.anim.fade_in);
    }

    private void showResultButton(Boolean hasResult) {
        showResultButton.setVisibility(hasResult ? View.VISIBLE : View.GONE);

    }




    public static class ViewBinder extends AbstractViewBinder<TestInfoViewHolder, TestInfoViewModel> {
        public ViewBinder(TestInfoViewHolder viewHolder, TestInfoViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestInfoObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(viewHolder::populateTestInfo, ErrorUtils.onError()));

            s.add(viewModel.getHasResultObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(viewHolder::showResultButton, ErrorUtils.onError()));

            s.add(RxView.clicks(viewHolder.startFab)
                    .subscribe(v -> ActivityFragments.getInstance().openTest(viewModel.getTestId())));
            s.add(RxView.clicks(viewHolder.showResultButton)
                    .subscribe(v-> showResult()));
        }

        private void showResult() {
            ActivityFragments.getInstance().openTestResult(viewModel.getTestId());
        }


    }


}
