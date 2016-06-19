package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultViewModel extends AbstractViewModel {

    private BehaviorSubject<Test> testSubject = BehaviorSubject.create();
    private BehaviorSubject<Integer> resultValueSubject = BehaviorSubject.create();
    private BehaviorSubject<TestResult> testResultSubject = BehaviorSubject.create();

    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(testSubject.asObservable()
                .switchMap(test -> resultValueSubject.asObservable()
                        .map(value -> getResult(test, value)))
                .subscribe(testResultSubject::onNext));
    }

    private TestResult getResult(Test test, Integer value) {
        for (TestResult testResult : test.getResults()) {
            if (testResult.getFrom() >= value && testResult.getTo() <= value)
                return testResult;
        }
        throw new IllegalStateException("Unable to find result for selected value");
    }

    public Observable<TestResult> getTestResultObservable()
    {
        return testResultSubject.asObservable();
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    public void setTest(Test test) {
        testSubject.onNext(test);
    }

    public void setResultValue(Integer result) {
        resultValueSubject.onNext(result);
    }

    public Observable<TestInfo> getTestInfoObservable() {
        return testSubject.asObservable().filter(this::notNull).map(Test::getInfo);
    }

}
