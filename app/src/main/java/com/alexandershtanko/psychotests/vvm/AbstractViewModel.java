package com.alexandershtanko.psychotests.vvm;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public abstract class AbstractViewModel {
    CompositeSubscription s=new CompositeSubscription();

    public void subscribe()
    {
        onSubscribe(s);
    }

    public  void unsubscribe()
    {
        s.unsubscribe();
    }


    public abstract void saveInstanceState();
    public abstract void restoreInstanceState();

    protected abstract void onSubscribe(CompositeSubscription s);
}
