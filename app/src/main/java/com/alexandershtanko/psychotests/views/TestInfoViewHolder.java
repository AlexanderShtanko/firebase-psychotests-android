package com.alexandershtanko.psychotests.views;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import io.codetail.animation.ViewAnimationUtils;
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
    @BindView(R.id.view_bg)
    View bgView;
    @BindView(R.id.layout_content)
    View contentLayout;


    @BindView(R.id.like)
    FloatingActionButton like;
    @BindView(R.id.dislike)
    FloatingActionButton dislike;
    private boolean flgShown = false;

    public TestInfoViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
        startFab.setVisibility(View.GONE);
        Animate.show(startFab, R.anim.scale_in);
    }


    public void populateLikeStatus(Boolean likeStatus) {
        if (likeStatus == null) {
            dislike.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
            like.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));

        } else if (likeStatus) {
            like.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.orange)));
            dislike.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
        } else {
            dislike.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.orange)));
            like.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
        }

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
                }
            });
        } else {
            image.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.tree_bg).bitmapTransform(new CropCircleTransformation(getContext())).into(image);

        }
        if (!flgShown) {
            show(contentLayout);
            flgShown = true;
        }
    }

    private void show(View contentLayout) {
        int cx = (contentLayout.getLeft() + contentLayout.getRight()) / 2;
        int cy = (contentLayout.getTop() + contentLayout.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, contentLayout.getWidth() - cx);
        int dy = Math.max(cy, contentLayout.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(contentLayout, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.start();
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

            s.add(viewModel.getLikeStatusObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateLikeStatus, ErrorUtils.onError()));


            s.add(RxView.clicks(viewHolder.startFab)
                    .subscribe(v -> ActivityFragments.getInstance().openTest(viewModel.getTestId())));
            s.add(RxView.clicks(viewHolder.showResultButton)
                    .subscribe(v -> showResult(), ErrorUtils.onError()));

            s.add(RxView.clicks(viewHolder.like).subscribe(v -> like(), ErrorUtils.onError()));
            s.add(RxView.clicks(viewHolder.dislike).subscribe(v -> dislike(), ErrorUtils.onError()));
        }

        private void dislike() {
            viewModel.dislike();
        }

        private void like() {
            viewModel.like();
        }


        private void showResult() {
            ActivityFragments.getInstance().openTestResult(viewModel.getTestId());
        }


    }


}
