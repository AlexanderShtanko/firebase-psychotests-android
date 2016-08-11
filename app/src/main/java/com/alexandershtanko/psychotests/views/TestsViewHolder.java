package com.alexandershtanko.psychotests.views;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.activities.MainActivity;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.viewmodels.TestsViewModel;
import com.alexandershtanko.psychotests.views.adapters.TestsAdapter;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;
import com.google.firebase.crash.FirebaseCrash;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsViewHolder extends AbstractViewHolder {
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.layout_internet_connection)
    View internetConnectionLayout;
    TestsAdapter adapter;

    public TestsViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
        adapter = new TestsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    private void populateEmpty(Boolean isEmpty) {
        internetConnectionLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

    }

    public static class ViewBinder extends AbstractViewBinder<TestsViewHolder, TestsViewModel> {

        private static final String TAG = ActivityViewHolder.ViewBinder.class.getSimpleName();
        private final MainActivity activity;

        public ViewBinder(Activity activity, TestsViewHolder viewHolder, TestsViewModel viewModel) {
            super(viewHolder, viewModel);
            this.activity = (MainActivity) activity;

            viewModel.getSortedCallback().setAdapter(viewHolder.adapter);
            viewHolder.adapter.setList(viewModel.getSortedList());
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getErrorObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::showError, ErrorUtils.onError()));
            s.add(Observable.create((Observable.OnSubscribe<String>) subscriber -> viewHolder.adapter.setOnItemClickListener(subscriber::onNext))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::selectTest));
            s.add(viewModel.getFilterObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::populateToolbar));
            s.add(viewModel.getEmptyObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populateEmpty));
        }

        private void populateToolbar(TestsViewModel.Filter filter) {
            if (filter.getOnlyDone()) {
                activity.updateToolbar(R.string.tests_done);
            } else
            if(filter.getOnlyFavorite())
            {
                activity.updateToolbar(R.string.tests_favorite);

            }
            else
            {
                if (filter.getCategory() != null) {
                    activity.updateToolbar(viewHolder.getContext().getResources().getString(R.string.category) + ": " + filter.getCategory());

                } else {
                    activity.updateToolbar(R.string.all_tests);

                }
            }
        }

        private void selectTest(String testId) {
            ActivityFragments.getInstance().openTestInfo(testId);

        }

        public void showError(Throwable throwable) {
            Log.e(TAG,"error:",throwable);
            FirebaseCrash.report(throwable);
            Snackbar.make(viewHolder.getView(), R.string.base_error,Snackbar.LENGTH_LONG).show();
        }


    }
}
