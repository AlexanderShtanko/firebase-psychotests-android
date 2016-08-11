package com.alexandershtanko.psychotests.vvm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class AbstractActivity<H extends AbstractViewHolder, M extends AbstractViewModel> extends AppCompatActivity implements ViewViewModel<H,M>{
    H viewHolder;
    M viewModel;
    AbstractViewBinder<H, M> viewBinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = createViewModel();

        super.onCreate(savedInstanceState);

        viewHolder = createViewHolder();
        setContentView(viewHolder.getView());

        viewBinder = createViewBinder(viewHolder,viewModel);
        viewModel.subscribe();


    }

    @Override
    protected void onPause() {
        super.onPause();
        viewBinder.unbind();

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinder.bind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
    }
}
