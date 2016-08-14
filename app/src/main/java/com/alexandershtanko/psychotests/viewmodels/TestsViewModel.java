package com.alexandershtanko.psychotests.viewmodels;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.util.Log;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.utils.RxPaper;
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

    private static final String TAG = TestsViewModel.class.getSimpleName();
    int counter = 0;
    private SortedCallback callback;
    private SortedList<TestInfo> sortedList;

    private BehaviorSubject<Filter> filterSubject = BehaviorSubject.create();
    private BehaviorSubject<Throwable> errorSubject = BehaviorSubject.create();
    private BehaviorSubject<Test> testSubject = BehaviorSubject.create();
    private BehaviorSubject<SortedCallback.SortType> sortTypeSubject = BehaviorSubject.create();

    private BehaviorSubject<Boolean> emptySubject = BehaviorSubject.create(true);


    public TestsViewModel(Context context) {

        callback = new SortedCallback();
        sortedList = new SortedList<>(TestInfo.class, callback);
    }

    @Override
    protected void onSubscribe(CompositeSubscription s) {

        s.add(filterSubject.asObservable()
                .switchMap(filter -> Storage.getInstance().getTestsObservable()
                        .onBackpressureBuffer().map(tests -> filter(filter, tests)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::onError)
                .subscribe(this::addToSortedList, this::onError));

        s.add(Storage.getInstance().getLikeStatusObservable().skip(1).subscribeOn(Schedulers.io()).map(map -> {
            List<Test> tests = new ArrayList<>();
            for (RxPaper.PaperObject<Boolean> obj : map.values()) {
                Test test = Storage.getInstance().getTest(obj.getKey());
                if (test != null) {
                    tests.add(test);
                }
            }
            return filter(filterSubject.getValue(), tests);
        })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::onError)
                .subscribe(this::addToSortedList));

        s.add(Storage.getInstance().getResultsObservable().skip(1).subscribeOn(Schedulers.io()).map(map -> {
            List<Test> tests = new ArrayList<>();
            for (RxPaper.PaperObject<Object> obj : map.values()) {
                Test test = Storage.getInstance().getTest(obj.getKey());
                if (test != null) {
                    tests.add(test);
                }
            }
            return filter(filterSubject.getValue(), tests);
        })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::onError)
                .subscribe(this::addToSortedList));

        s.add(sortTypeSubject.asObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(sortType -> {
            callback.setSortType(sortType);
            List<TestInfo> list = new ArrayList<>();
            sortedList.beginBatchedUpdates();
            for (int i = 0; i < sortedList.size(); i++) {
                list.add(sortedList.get(i));
            }
            sortedList.clear();
            for (int i = 0; i < list.size(); i++) {
                sortedList.add(list.get(i));
            }

            sortedList.endBatchedUpdates();
        }));

    }


    private void addToSortedList(List<FilterResult<Test>> filterResults) {

        for (FilterResult<Test> filterResult : filterResults) {
            if (filterResult != null && filterResult.getObject() != null) {
                counter++;
                Test test = filterResult.getObject();


                if (filterResult.getResult()) {
                    sortedList.add(test.getInfo());
                    Log.e(TAG, counter + "|" + sortedList.size() + " added: " + test.getInfo().getName());
                } else {
                    sortedList.remove(test.getInfo());
                    Log.e(TAG, counter + "|" + sortedList.size() + " removed: " + test.getInfo().getName());

                }


            }
        }

        if (sortedList != null && sortedList.size() > 0)
            updateEmpty(false);
        else
            updateEmpty(true);
    }

    private void updateEmpty(Boolean isEmpty) {
        if (isEmpty != emptySubject.getValue())
            emptySubject.onNext(isEmpty);
    }

    private List<FilterResult<Test>> filter(Filter filter, List<Test> tests) {
        List<FilterResult<Test>> filterResults = new ArrayList<>();

        for (Test test : tests) {

            test.getInfo().setLikeStatus(Storage.getInstance().getLikeStatus(test.getInfo().getTestId()));
            test.getInfo().setDone(Storage.getInstance().hasResult(test.getInfo().getTestId()));

            FilterResult<Test> filterResult = new FilterResult<>(false, test);

            String testOfDayId = null;
            if (filter.isEmpty())
                testOfDayId = Storage.getInstance().getTestOfDayId();

            if (filter.getCategory() == null || test.getInfo().getCategory() != null && test.getInfo().getCategory().equals(filter.getCategory())) {
                boolean isDone = test.getInfo().isDone();
                if (!filter.getOnlyDone() || isDone) {
                    Boolean isFavorite = test.getInfo().getLikeStatus();
                    if (!filter.getOnlyFavorite() || isFavorite != null && isFavorite) {

                        if (filter.isEmpty()) {
                            if (test.getInfo().getTestId().equals(testOfDayId))
                                test.getInfo().setTestOfDay(true);
                        }


                        filterResult.setResult(true);
                    }
                }
            }
            filterResults.add(filterResult);
        }


        return filterResults;
    }

    public void setSortType(SortedCallback.SortType sortType) {
        sortTypeSubject.onNext(sortType);
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

    public void setFilter(String category, Boolean onlyDone, Boolean onlyFavorite) {
        filterSubject.onNext(new Filter(category, onlyDone, onlyFavorite));
    }

    public Observable<Filter> getFilterObservable() {
        return filterSubject.asObservable();
    }

    public static class FilterResult<T> {


        private Boolean result;
        private T object;

        public FilterResult(Boolean result, T object) {
            this.result = result;
            this.object = object;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }

        public T getObject() {
            return object;
        }
    }

    public static class Filter {
        private final Boolean onlyFavorite;
        private String category;
        private Boolean onlyDone;

        public Filter(String category, Boolean onlyDone, Boolean onlyFavorite) {
            this.category = category;
            this.onlyDone = onlyDone;
            this.onlyFavorite = onlyFavorite;
        }

        public String getCategory() {
            return category;
        }

        public Boolean getOnlyDone() {
            return onlyDone;
        }

        public Boolean getOnlyFavorite() {
            return onlyFavorite;
        }

        public Boolean isEmpty() {
            return (onlyFavorite == null || !onlyFavorite)
                    && (onlyDone == null || !onlyDone)
                    && (category == null);
        }
    }
}
