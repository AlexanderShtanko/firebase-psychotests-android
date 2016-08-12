package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.models.TestQuestion;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestViewModel extends AbstractViewModel {

    private BehaviorSubject<Test> testSubject = BehaviorSubject.create();
    private BehaviorSubject<Integer> currentQuestionIndexSubject = BehaviorSubject.create();
    private BehaviorSubject<TestQuestion> currentQuestionSubject = BehaviorSubject.create();
    private BehaviorSubject<String> testIdSubject = BehaviorSubject.create();
    private List<Integer> resultList = new ArrayList<>();

    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(testIdSubject.asObservable().switchMap(Storage.getInstance()::getTestObservable).first()
                .subscribeOn(Schedulers.io()).subscribe(testSubject::onNext));

        s.add(testSubject.asObservable().subscribeOn(Schedulers.io()).subscribe(test -> currentQuestionIndexSubject.onNext(0)));

        s.add(currentQuestionIndexSubject.asObservable()
                .switchMap(index -> testSubject.asObservable().first()
                        .filter(test -> index < test.getQuestions().size())
                        .map(test -> test.getQuestions().get(index)))
                .subscribe(currentQuestionSubject::onNext));

        s.add(currentQuestionIndexSubject.asObservable()
                .switchMap(index -> testSubject.asObservable()
                        .filter(test -> index == test.getQuestions().size()))
                .subscribeOn(Schedulers.io())
                .subscribe(test -> Storage.getInstance().setResult(test.getInfo().getTestId(), resultList)));
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }


    public void selectVariant(Integer value) {
        int currentQuestionIndex = getCurrentQuestionIndex();

        if (resultList.size() <= currentQuestionIndex)
            resultList.add(value);
        else resultList.set(currentQuestionIndex, value);

        if (currentQuestionIndexSubject.getValue() < getQuestionsCount())
            currentQuestionIndexSubject.onNext(currentQuestionIndexSubject.getValue() + 1);
    }

    public void back() {
        Integer currentQuestionIndex = getCurrentQuestionIndex();
        if (currentQuestionIndex != null && currentQuestionIndex > 0)
            currentQuestionIndexSubject.onNext(currentQuestionIndex - 1);
    }

    public Observable<TestQuestion> getCurrentQuestionObservable() {
        return currentQuestionSubject.asObservable();
    }

    public Integer getQuestionsCount() {
        if (testSubject.getValue() != null)
            return testSubject.getValue().getQuestions().size();
        return 0;
    }

    public Observable<Integer> getCurrentQuestionIndexObservable() {
        return currentQuestionIndexSubject.asObservable();
    }


    public Integer getCurrentQuestionIndex() {
        return currentQuestionIndexSubject.getValue();
    }

    public String getTestId() {
        return testSubject.getValue().getInfo().getTestId();
    }

    public void setTestId(String testId) {
        testIdSubject.onNext(testId);
    }

    public Observable<String> getTestNameObservable() {
        return testSubject.asObservable().filter(this::notNull).map(Test::getInfo).map(TestInfo::getName);
    }

    public Observable<String> getTestImageObservable() {
        return testSubject.asObservable().filter(this::notNull).map(Test::getInfo).map(TestInfo::getImage).filter(this::notNull);
    }
}
