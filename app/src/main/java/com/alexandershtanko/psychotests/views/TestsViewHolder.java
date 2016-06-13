package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.TestsViewModel;
import com.alexandershtanko.psychotests.views.adapters.TestsAdapter;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsViewHolder extends AbstractViewHolder {
    @BindView(R.id.list)
    RecyclerView list;
    TestsAdapter adapter;

    public TestsViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
        adapter = new TestsAdapter();
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
    }

    public static class ViewBinder extends AbstractViewBinder<TestsViewHolder,TestsViewModel> {


        public ViewBinder(TestsViewHolder viewHolder, TestsViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {

        }
    }
}
