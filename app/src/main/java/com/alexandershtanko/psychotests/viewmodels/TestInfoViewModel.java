package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestInfoViewModel extends AbstractViewModel {
    BehaviorSubject<TestInfo> testInfoSubject = BehaviorSubject.create();
    @Override
    protected void onSubscribe(CompositeSubscription s) {

    }

    public void setTestInfo(TestInfo testInfo)
    {
        testInfoSubject.onNext(testInfo);
    }

    public Observable<TestInfo> getTestInfoObservable()
    {
        return testInfoSubject.asObservable();
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }
}
