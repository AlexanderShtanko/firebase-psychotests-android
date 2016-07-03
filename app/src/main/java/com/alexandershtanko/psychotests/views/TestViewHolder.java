package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.models.AnswerVariant;
import com.alexandershtanko.psychotests.models.TestQuestion;
import com.alexandershtanko.psychotests.utils.Animate;
import com.alexandershtanko.psychotests.utils.DisplayUtils;
import com.alexandershtanko.psychotests.utils.Storage;
import com.alexandershtanko.psychotests.viewmodels.TestViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import butterknife.BindView;
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

    public TestViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public void populateNumber(Integer index, Integer count)
    {
        number.setText(getContext().getString(R.string.number,index+1 ,count));
    }

    public static class ViewBinder extends AbstractViewBinder<TestViewHolder, TestViewModel> {
        Storage storage;

        public ViewBinder(TestViewHolder viewHolder, TestViewModel viewModel) {
            super(viewHolder, viewModel);
            storage = new Storage(viewHolder.getContext());
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getCurrentQuestionObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::populateQuestion));
            s.add(viewModel.getCurrentQuestionIndexObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(index->viewHolder.populateNumber(index,viewModel.getQuestionsCount())));
        }


        public void populateQuestion(TestQuestion question) {

            viewHolder.question.setVisibility(View.GONE);
            viewHolder.variants.setVisibility(View.GONE);

            viewHolder.question.setText(question.getText());
            viewHolder.variants.removeAllViews();

            for (AnswerVariant variant : question.getVariants()) {
                TextView variantText = (TextView) LayoutInflater.from(viewHolder.getContext()).inflate(R.layout.item_variant, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0, DisplayUtils.getSizeInPx(viewHolder.getContext(),10));
                variantText.setLayoutParams(params);

                variantText.setClickable(true);
                variantText.setText(variant.getText());
                variantText.setOnClickListener(v -> selectVariant(variant));
                viewHolder.variants.addView(variantText);
            }


            Animate.show(viewHolder.question,R.anim.expand_from_top);
            Animate.show(viewHolder.variants,R.anim.expand_from_bottom);
        }

        private void selectVariant(AnswerVariant variant) {
            viewModel.selectVariant(variant.getValue());
            if(viewModel.getCurrentQuestionIndex()==viewModel.getQuestionsCount())
            {
                storage.saveResult(viewModel.getTestId(),viewModel.getResult());
                ActivityFragments.getInstance().openTestResult(viewModel.getResult());
            }
        }
    }
}
