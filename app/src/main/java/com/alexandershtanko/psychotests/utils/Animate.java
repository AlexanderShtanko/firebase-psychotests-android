package com.alexandershtanko.psychotests.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by aleksandr on 03.07.16.
 */
public class Animate {

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);

                v.setAlpha(interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.setAlpha(1f - interpolatedTime);

                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static boolean show(View view, Integer animRes) {

        if (view.getVisibility() == View.VISIBLE) {
            return false;
        }

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), animRes);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
        return true;

    }

    public static boolean hide(View view, int animRes) {

        if (view.getVisibility() == View.GONE)
            return false;

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), animRes);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);

        return true;
    }

    public static void animateImageView(ImageView view) {

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.setDuration(1000).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 5f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 5f));

        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playSequentially(
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 50).setDuration(5000),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 50).setDuration(5000),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -50).setDuration(10000),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -50).setDuration(10000),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0).setDuration(10000),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0).setDuration(10000)

        );

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.playSequentially(animatorSet1, animatorSet2);
        animatorSet.setInterpolator(new LinearInterpolator());

        AnimatorSet repeatAnimatorSet = new AnimatorSet();
        repeatAnimatorSet.play(animatorSet).after(0);
        repeatAnimatorSet.start();


    }
}