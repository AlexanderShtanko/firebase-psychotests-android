package com.alexandershtanko.psychotests.vvm;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public abstract class AbstractViewBinder<H extends AbstractViewHolder, M extends AbstractViewModel> {
    protected final H viewHolder;
    protected final M viewModel;

    CompositeSubscription s;

    public AbstractViewBinder(H viewHolder, M viewModel) {
        this.viewHolder = viewHolder;
        this.viewModel = viewModel;
    }

    public void bind() {
        s = new CompositeSubscription();
        onBind(s);
    }

    public void unbind() {
        s.unsubscribe();
        s = null;
    }

    protected abstract void onBind(CompositeSubscription s);
}
