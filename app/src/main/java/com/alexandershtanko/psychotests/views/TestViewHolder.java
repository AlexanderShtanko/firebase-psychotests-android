package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.models.AnswerVariant;
import com.alexandershtanko.psychotests.models.TestQuestion;
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
        number.setText(getContext().getString(R.string.number,index,count));
    }

    public static class ViewBinder extends AbstractViewBinder<TestViewHolder, TestViewModel> {


        public ViewBinder(TestViewHolder viewHolder, TestViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getCurrentQuestionObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::populateQuestion));
            s.add(viewModel.getCurrentQuestionIndexObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(index->viewHolder.populateNumber(index,viewModel.getQuestionsCount())));
            s.add(viewModel.getCurrentQuestionIndexObservable()
                    .filter(index->index.equals(viewModel.getQuestionsCount()))
                    .switchMap(i->viewModel.getResultObservable())
                    .doOnNext(viewModel::saveResult)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(r->ActivityFragments.getInstance().openTestResult()));
        }


        public void populateQuestion(TestQuestion question) {
            viewHolder.question.setText(question.getText());
            viewHolder.variants.removeAllViews();

            for (AnswerVariant variant : question.getVariants()) {
                TextView variantText = (TextView) LayoutInflater.from(viewHolder.getContext()).inflate(R.layout.item_variant, viewHolder.variants);
                variantText.setClickable(true);
                variantText.setText(variant.getText());
                variantText.setOnClickListener(v -> viewModel.selectVariant(variant.getValue()));
            }
        }
    }
}
