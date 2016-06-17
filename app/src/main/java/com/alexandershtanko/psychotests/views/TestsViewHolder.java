package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.viewmodels.TestsViewModel;
import com.alexandershtanko.psychotests.views.adapters.TestsAdapter;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsViewHolder extends AbstractViewHolder {
    @BindView(R.id.list)
    RecyclerView recyclerView;
    TestsAdapter adapter;

    public TestsViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
        adapter = new TestsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public static class ViewBinder extends AbstractViewBinder<TestsViewHolder, TestsViewModel> {

        public ViewBinder(TestsViewHolder viewHolder, TestsViewModel viewModel) {
            super(viewHolder, viewModel);
            viewHolder.adapter.setList(viewModel.getSortedList());
            viewModel.getSortedCallback().setAdapter(viewHolder.adapter);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getErrorObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::showError, ErrorUtils.onError()));

        }

        public void showError(Throwable throwable)
        {
            Toast.makeText(viewHolder.getContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
        }


    }
}
