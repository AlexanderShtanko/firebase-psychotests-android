package com.alexandershtanko.psychotests.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.activities.MainActivity;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.helpers.AmplitudeHelper;
import com.alexandershtanko.psychotests.models.AnswerVariant;
import com.alexandershtanko.psychotests.models.TestQuestion;
import com.alexandershtanko.psychotests.utils.DisplayUtils;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.utils.StringUtils;
import com.alexandershtanko.psychotests.viewmodels.TestViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestViewHolder extends AbstractViewHolder {
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.layout_variants)
    LinearLayout variants;
    @BindView(R.id.background)
    ImageView backgroundImage;
    @BindView(R.id.layout_test)
    View testLayout;

    public TestViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public void populateNumber(Integer index, Integer count) {
        if (index >= count)
            index = count - 1;

        number.setText(getContext().getString(R.string.number, index + 1, count));
    }

    public static class ViewBinder extends AbstractViewBinder<TestViewHolder, TestViewModel> {

        private final MainActivity activity;

        public ViewBinder(Activity activity, TestViewHolder viewHolder, TestViewModel viewModel) {
            super(viewHolder, viewModel);
            this.activity = (MainActivity) activity;
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getTestNameObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::populateToolbar, ErrorUtils.onError()));
            s.add(viewModel.getCurrentQuestionObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::populateQuestion,ErrorUtils.onError()));

            s.add(viewModel.getCurrentQuestionIndexObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(index -> viewHolder
                            .populateNumber(index, viewModel.getQuestionsCount()),ErrorUtils.onError()));
            s.add(viewModel.getTestImageObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::populateBackground,ErrorUtils.onError()));
        }

        private void populateToolbar(String name) {
            activity.updateToolbar(name);
        }

        private void populateBackground(String background) {
            Glide.with(viewHolder.getContext()).load(background).bitmapTransform(new SketchFilterTransformation(viewHolder.getContext())).into(viewHolder.backgroundImage);

        }


        public void populateQuestion(TestQuestion question) {

            viewHolder.question.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                viewHolder.question.setText(question.getText());
                viewHolder.question.animate().alpha(1f).setDuration(300).start();
            }).start();


            viewHolder.variants.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                viewHolder.variants.removeAllViews();

                for (AnswerVariant variant : question.getVariants()) {
                    TextView variantText = (TextView) LayoutInflater.from(viewHolder.getContext()).inflate(R.layout.item_variant, null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, DisplayUtils.getSizeInPx(viewHolder.getContext(), 10));
                    variantText.setLayoutParams(params);

                    variantText.setClickable(true);
                    variantText.setText(StringUtils.capitalizeSentences(variant.getText()));
                    variantText.setOnClickListener(v -> selectVariant(variant));
                    viewHolder.variants.addView(variantText);
                }
                viewHolder.variants.animate().alpha(1f).setDuration(300).start();


            });


        }

        private void selectVariant(AnswerVariant variant) {
            viewModel.selectVariant(variant.getValue());
            if (viewModel.getCurrentQuestionIndex() >= viewModel.getQuestionsCount()) {
                {
                    AmplitudeHelper.testDone(viewModel.getTestId(),viewModel.getTest().getInfo().getName(),viewModel.getTest().getInfo().getCategory());
                    ActivityFragments.getInstance().openTestResult(viewModel.getTestId());
                }
            }
        }
    }
}
