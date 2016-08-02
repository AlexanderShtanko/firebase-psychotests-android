package com.alexandershtanko.psychotests.viewmodels;

import android.content.Context;
import android.support.v7.util.SortedList;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.views.adapters.SortedCallback;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsViewModel extends AbstractViewModel {

    private Storage storage = Storage.getInstance();


    private SortedCallback callback;
    private SortedList<TestInfo> sortedList;

    private BehaviorSubject<Filter> filterSubject = BehaviorSubject.create();
    private BehaviorSubject<Throwable> errorSubject = BehaviorSubject.create();

    private BehaviorSubject<Boolean> emptySubject = BehaviorSubject.create(false);


    public TestsViewModel(Context context) {

        callback = new SortedCallback();
        sortedList = new SortedList<>(TestInfo.class, callback);
    }

    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(filterSubject.asObservable()
                .switchMap(filter -> storage.getTestsObservable()
                        .map(tests -> filter(tests, filter)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addToSortedList, this::onError));

    }


    private void addToSortedList(List<Test> tests) {
        String testOfDayId=storage.getTestOfDayId();

        if (tests != null && tests.size() > 0) {
            if (emptySubject.getValue())
                emptySubject.onNext(false);
            for (Test test : tests) {
                if(test.getInfo().getTestId().equals(testOfDayId))
                    test.getInfo().setTestOfDay(true);
                sortedList.add(test.getInfo());
            }
        } else if (!emptySubject.getValue())
            emptySubject.onNext(true);


    }

    private List<Test> filter(List<Test> tests, Filter filter) {
        List<Test> filteredTests = new ArrayList<>();
        String category = filter.getCategory();
        Boolean onlyDone = filter.getOnlyDone();

        for (Test test : tests) {
            if (category == null || test.getInfo().getCategory() != null && test.getInfo().getCategory().equals(category)) {
                boolean isDone = storage.hasResult(test.getInfo().getTestId());
                if (!onlyDone || isDone) {
                    if (test.getInfo() != null)
                        test.getInfo().setDone(isDone);
                    filteredTests.add(test);
                }

            }

        }

        return filteredTests;
    }

    public Observable<Boolean> getEmptyObservable() {
        return emptySubject.asObservable();
    }


    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    public SortedCallback getSortedCallback() {
        return callback;
    }

    public SortedList<TestInfo> getSortedList() {
        return sortedList;
    }

    public void setFilter(String category, Boolean onlyDone) {
        filterSubject.onNext(new Filter(category, onlyDone));
    }

    public Observable<Filter> getFilterObservable() {
        return filterSubject.asObservable();
    }

    public static class Filter {
        private String category;
        private Boolean onlyDone;

        public Filter(String category, Boolean onlyDone) {
            this.category = category;
            this.onlyDone = onlyDone;
        }

        public String getCategory() {
            return category;
        }

        public Boolean getOnlyDone() {
            return onlyDone;
        }
    }
}
