package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestInfoViewModel extends AbstractViewModel {
    Storage storage = Storage.getInstance();
    BehaviorSubject<String> testIdSubject = BehaviorSubject.create();
    BehaviorSubject<TestInfo> testInfoSubject = BehaviorSubject.create();
    BehaviorSubject<Boolean> hasResultSubject = BehaviorSubject.create();
    BehaviorSubject<Boolean> likeStatusSubject = BehaviorSubject.create();


    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(testIdSubject.asObservable()
                .switchMap(storage::getTestObservable)
                .filter(this::notNull)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(Test::getInfo)
                .subscribe(testInfoSubject::onNext));

        s.add(testIdSubject.asObservable()
                .map(storage::hasResult)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(hasResultSubject::onNext));
        s.add(testIdSubject.asObservable().switchMap(storage::getLikeStatusObservable).subscribeOn(Schedulers.io()).subscribe(
                likeStatusSubject::onNext
        ));
    }

    public Observable<TestInfo> getTestInfoObservable() {
        return testInfoSubject.asObservable();
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    public String getTestId() {
        return testIdSubject.getValue();
    }

    public void setTestId(String testId) {
        testIdSubject.onNext(testId);
    }

    public BehaviorSubject<Boolean> getHasResultObservable() {
        return hasResultSubject;
    }

    public void like() {
        storage.setLikeStatus(testIdSubject.getValue(), true);
    }

    public void dislike() {
        storage.setLikeStatus(testIdSubject.getValue(), false);

    }

    public Observable<Boolean> getLikeStatusObservable() {
        return likeStatusSubject.asObservable();
    }
}
