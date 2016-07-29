package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class CategoriesViewModel extends AbstractViewModel {
    Storage storage = Storage.getInstance();
    Set<String> categories = new HashSet<>();
    private BehaviorSubject<Throwable> errorSubject = BehaviorSubject.create();
    private BehaviorSubject<String> categorySubject = BehaviorSubject.create();
    private BehaviorSubject<Boolean> emptySubject = BehaviorSubject.create(true);


    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(storage.getTestsObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(this::observeData, this::onError));

    }

    private void observeData(List<Test> tests) {

        for (Test test : tests) {
            TestInfo testInfo = test.getInfo();
            String category = testInfo.getCategory();

            if (!categories.contains(category)) {
                categorySubject.onNext(category);
                categories.add(category);
            }
        }
        if(categories.size()==0)
            emptySubject.onNext(true);
        else emptySubject.onNext(false);
    }

    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }

    public Observable<String> getCategoryObservable() {
        return categorySubject.asObservable();
    }

    public Observable<Throwable> getErrorObservable() {
        return errorSubject.asObservable();
    }


    public void onError(Throwable throwable) {
        errorSubject.onNext(throwable);
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    public Observable<Boolean> getEmptyObservable()
    {
        return emptySubject.asObservable();
    }
}
