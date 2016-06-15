package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.CategoriesViewModel;
import com.alexandershtanko.psychotests.views.adapters.CategoriesAdapter;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class CategoriesViewHolder extends AbstractViewHolder {
    @BindView(R.id.list)
    RecyclerView list;

    CategoriesAdapter adapter;

    public CategoriesViewHolder(Context context, int layoutRes) {
        super(context, layoutRes);
        adapter = new CategoriesAdapter();
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
    }

    public void populate(List<String> categoriesList)
    {
        adapter.add(categoriesList);
    }

    public static class ViewBinder extends AbstractViewBinder<CategoriesViewHolder, CategoriesViewModel> {
        public ViewBinder(CategoriesViewHolder viewHolder, CategoriesViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
        }
    }
}
