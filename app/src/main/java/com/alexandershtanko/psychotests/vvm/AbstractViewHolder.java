package com.alexandershtanko.psychotests.vvm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by aleksandr on 12.06.16.
 */
public abstract class AbstractViewHolder {

    private final Context context;
    private final View view;

    public AbstractViewHolder(Context context, int layoutRes) {
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(layoutRes,null);
        ButterKnife.bind(this,getView());

    }

    public Context getContext() {
        return context;
    }

    public View getView() {
        return view;
    }
}
