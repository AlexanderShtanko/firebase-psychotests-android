package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.helpers.AmplitudeHelper;
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

    BehaviorSubject<String> testIdSubject = BehaviorSubject.create();
    BehaviorSubject<TestInfo> testInfoSubject = BehaviorSubject.create();
    BehaviorSubject<Boolean> hasResultSubject = BehaviorSubject.create();
    BehaviorSubject<Boolean> likeStatusSubject = BehaviorSubject.create();


    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(testIdSubject.asObservable()
                .switchMap(Storage.getInstance()::getTestObservable)
                .filter(this::notNull)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(Test::getInfo)
                .subscribe(testInfoSubject::onNext));

        s.add(testIdSubject.asObservable()
                .map(Storage.getInstance()::hasResult)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(hasResultSubject::onNext));
        s.add(testIdSubject.asObservable().switchMap(Storage.getInstance()::getLikeStatusObservable).subscribeOn(Schedulers.io()).subscribe(
                likeStatusSubject::onNext
        ));

        s.add(testInfoSubject.asObservable().observeOn(Schedulers.io()).subscribe(testInfo->{
            if(testInfo!=null)
            {
                boolean testOfDay = testInfo.getTestId().equals(Storage.getInstance().getTestOfDayId());
                AmplitudeHelper.onOpenTestInfo(testInfo.getTestId(),testInfo.getName(),testInfo.getCategory(),Storage.getInstance().hasResult(testInfo.getTestId()),testOfDay);
            }
        }));
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

        Boolean likeStatus = Storage.getInstance().getLikeStatus(testIdSubject.getValue());
        if (likeStatus != null && likeStatus)
            Storage.getInstance().removeLikeStatus(testIdSubject.getValue());
        else
            Storage.getInstance().setLikeStatus(testIdSubject.getValue(), true);
    }

    public void dislike() {
        Boolean likeStatus = Storage.getInstance().getLikeStatus(testIdSubject.getValue());
        if (likeStatus != null && !likeStatus)
            Storage.getInstance().removeLikeStatus(testIdSubject.getValue());
        else
            Storage.getInstance().setLikeStatus(testIdSubject.getValue(), false);

    }

    public Observable<Boolean> getLikeStatusObservable() {
        return likeStatusSubject.asObservable();
    }
}
