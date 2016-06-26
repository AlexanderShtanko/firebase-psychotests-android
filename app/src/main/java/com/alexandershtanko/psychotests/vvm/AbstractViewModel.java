package com.alexandershtanko.psychotests.vvm;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public abstract class AbstractViewModel {
    CompositeSubscription s = new CompositeSubscription();

    public void subscribe() {
        s = new CompositeSubscription();
        onSubscribe(s);
    }

    public void unsubscribe() {
        s.unsubscribe();
        s = null;
    }


    public abstract void saveInstanceState();

    public abstract void restoreInstanceState();

    protected abstract void onSubscribe(CompositeSubscription s);

    public Boolean notNull(Object object) {
        return object != null;
    }
}
