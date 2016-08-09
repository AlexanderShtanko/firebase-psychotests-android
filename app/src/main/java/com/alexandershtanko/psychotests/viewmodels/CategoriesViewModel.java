package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Category;
import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class CategoriesViewModel extends AbstractViewModel {
    Map<String,Category> categories = new HashMap<>();
    private BehaviorSubject<Throwable> errorSubject = BehaviorSubject.create();
    private BehaviorSubject<Category> categorySubject = BehaviorSubject.create();
    private BehaviorSubject<Boolean> emptySubject = BehaviorSubject.create(true);


    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(Storage.getInstance().getTestsObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(this::observeData, this::onError));

    }

    private void observeData(List<Test> tests) {

        for (Test test : tests) {
            TestInfo testInfo = test.getInfo();
            String name = testInfo.getCategory();

            if (!categories.containsKey(name)) {
                Category category = new Category();
                category.setImage(testInfo.getImage());
                category.setName(name);

                categorySubject.onNext(category);
                categories.put(name,category);
            }
        }
        if(categories.size()==0)
            emptySubject.onNext(true);
        else emptySubject.onNext(false);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    public Observable<Category> getCategoryObservable() {
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
