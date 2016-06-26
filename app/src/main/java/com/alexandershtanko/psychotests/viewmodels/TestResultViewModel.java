package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestResultViewModel extends AbstractViewModel {

    private BehaviorSubject<Test> testSubject = BehaviorSubject.create();
    private BehaviorSubject<List<Integer>> resultValueSubject = BehaviorSubject.create();
    private BehaviorSubject<TestResult> testResultSubject = BehaviorSubject.create();

    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(testSubject.asObservable()
                .switchMap(test -> resultValueSubject.asObservable()
                        .map(value -> getResult(test, value)))
                .subscribe(testResultSubject::onNext));
    }

    private TestResult getResult(Test test, List<Integer> resultList) {

        int value = 0;

        for (int i = 0; i < resultList.size(); i++) {
            value += resultList.get(i);

        }
        int i=0;

        TestResult min = null;
        TestResult max = null;

        for (TestResult testResult : test.getResults()) {
            if (value>=testResult.getFrom() && value<=testResult.getTo())
                return testResult;

            if(min==null||min.getFrom()>testResult.getFrom())
                min = testResult;

            if(max==null||max.getTo()<testResult.getTo())
                max = testResult;

            i++;
        }

        if(min!=null && min.getFrom()>value)
            return min;

        if(max!=null && max.getTo()<value)
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

    public void setTest(Test test) {
        testSubject.onNext(test);
    }

    public void setResultValue(List<Integer> result) {
        resultValueSubject.onNext(result);
    }

    public Observable<TestInfo> getTestInfoObservable() {
        return testSubject.asObservable().filter(this::notNull).map(Test::getInfo);
    }

}
