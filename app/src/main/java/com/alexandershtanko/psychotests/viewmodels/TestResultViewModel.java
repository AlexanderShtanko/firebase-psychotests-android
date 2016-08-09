package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultViewModel extends AbstractViewModel {

    private Storage storage = Storage.getInstance();

    private BehaviorSubject<TestInfo> testInfoSubject = BehaviorSubject.create();
    private BehaviorSubject<TestResult> testResultSubject = BehaviorSubject.create();
    private BehaviorSubject<String> testIdSubject = BehaviorSubject.create();
    private BehaviorSubject<Boolean> likeStatusSubject = BehaviorSubject.create();


    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(testIdSubject.asObservable()
                .switchMap(storage::getTestObservable)
                .subscribeOn(Schedulers.io()).subscribe(test -> {
                    testInfoSubject.onNext(test.getInfo());
                    List<Integer> result = storage.getResult(test.getInfo().getTestId());
                    if (result != null) {
                        testResultSubject.onNext(getResult(test, result));
                    }
                }, this::onError));

        s.add(testIdSubject.asObservable().switchMap(storage::getLikeStatusObservable).subscribeOn(Schedulers.io()).subscribe(
                likeStatusSubject::onNext
        ));
    }

    private TestResult getResult(Test test, List<Integer> resultList) {

        int value = 0;

        for (int i = 0; i < resultList.size(); i++) {
            value += resultList.get(i);

        }
        int i = 0;

        TestResult min = null;
        TestResult max = null;

        for (TestResult testResult : test.getResults()) {
            if (value >= testResult.getFrom() && value <= testResult.getTo())
                return testResult;

            if (min == null || min.getFrom() > testResult.getFrom())
                min = testResult;

            if (max == null || max.getTo() < testResult.getTo())
                max = testResult;

            i++;
        }

        if (min != null && min.getFrom() > value)
            return min;

        if (max != null && max.getTo() < value)
            return max;

        return null;
    }

    public Observable<TestResult> getTestResultObservable() {
        return testResultSubject.asObservable();
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }


    public Observable<TestInfo> getTestInfoObservable() {
        return testInfoSubject.asObservable();
    }

    public String getTestId() {
        return testIdSubject.getValue();
    }

    public void setTestId(String testId) {
        testIdSubject.onNext(testId);
    }

    public TestInfo getTestInfo() {
        return testInfoSubject.getValue();
    }

    public TestResult getTestResult() {
        return testResultSubject.getValue();
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
