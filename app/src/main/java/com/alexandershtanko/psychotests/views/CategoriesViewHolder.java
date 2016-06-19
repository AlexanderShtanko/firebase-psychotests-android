package com.alexandershtanko.psychotests.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.viewmodels.CategoriesViewModel;
import com.alexandershtanko.psychotests.views.adapters.CategoriesAdapter;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import java.util.Set;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    public void populate(Set<String> categories)
    {
        adapter.populate(categories);
    }

    public void add(String category)
    {
        adapter.add(category);
    }

    public static class ViewBinder extends AbstractViewBinder<CategoriesViewHolder, CategoriesViewModel> {
        public ViewBinder(CategoriesViewHolder viewHolder, CategoriesViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(viewModel.getCategoriesObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::populate));
            s.add(viewModel.getCategoryObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder::add));
            s.add(viewModel.getErrorObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::showError));
        }

        public void showError(Throwable throwable)
        {
            Toast.makeText(viewHolder.getContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
