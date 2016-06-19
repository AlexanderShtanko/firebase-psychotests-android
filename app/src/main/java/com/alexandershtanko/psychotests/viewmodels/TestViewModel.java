package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.SessionManager;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestQuestion;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestViewModel extends AbstractViewModel {

    private BehaviorSubject<Test> testSubject = BehaviorSubject.create();
    private BehaviorSubject<Integer> currentQuestionIndexSubject = BehaviorSubject.create(0);
    private BehaviorSubject<Integer> resultSubject = BehaviorSubject.create(0);


    @Override
    protected void onSubscribe(CompositeSubscription s) {

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

    public Observable<Test> getTestObservable() {
        return testSubject.asObservable();
    }

    public void selectVariant(Integer value) {
        resultSubject.onNext(resultSubject.getValue() + value);
        currentQuestionIndexSubject.onNext(currentQuestionIndexSubject.getValue() + 1);
    }

    public Observable<TestQuestion> getCurrentQuestionObservable() {
        return currentQuestionIndexSubject.asObservable().filter(index->index<getQuestionsCount()).switchMap(index -> getTestObservable().map(test -> test.getQuestions().get(index)));
    }

    public Integer getQuestionsCount() {
        if (testSubject.getValue() != null)
            return testSubject.getValue().getQuestions().size();
        return 0;
    }

    public Observable<Integer> getCurrentQuestionIndexObservable() {
        return currentQuestionIndexSubject.asObservable();
    }


    public void saveResult(Integer result) {
        SessionManager.getInstance().setResult(result);
    }

    public Observable<Integer> getResultObservable() {
        return resultSubject.asObservable();
    }
}
