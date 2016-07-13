package com.alexandershtanko.psychotests.vvm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aleksandr on 12.06.16.
 */
public abstract class AbstractFragment<H extends AbstractViewHolder, M extends AbstractViewModel> extends Fragment implements ViewViewModel<H, M> {

    H viewHolder;
    M viewModel;
    AbstractViewBinder<H, M> viewBinder;
    Activity activity;

    public Activity getMainActivity() {
        return activity;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewBinder = createViewBinder(viewHolder, viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewHolder = createViewHolder();
        return viewHolder.getView();

    }

    @Override
    public void onPause() {
        super.onPause();
        viewBinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewBinder.bind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        viewModel = createViewModel();
        viewModel.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
    }
}
