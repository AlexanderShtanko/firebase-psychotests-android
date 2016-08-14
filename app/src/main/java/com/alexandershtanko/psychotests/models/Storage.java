package com.alexandershtanko.psychotests.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.alexandershtanko.psychotests.utils.RxPaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * Created by aleksandr on 17.11.15.
 */
public class Storage {

    public static final String TEST_OF_DAY = "test of day";
    private static final String BOOK_TESTS = "TEST";
    private static final String BOOK_RESULTS = "RESULT";
    private static final String BOOK_DATA = "DATA";
    private static final String BOOK_LIKE_STATUS = "LIKE_STATUS";
    private static Storage instance;

    private Context context;
    private RxPaper rxPaper;

    private Storage() {
    }

    public static synchronized Storage getInstance() {
        if (instance == null)
            instance = new Storage();
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        rxPaper = RxPaper.getInstance();
        rxPaper.init(context);
    }

    public void destroy() {
        rxPaper.destroy();
        context = null;
        rxPaper = null;
    }


    public void addTest(Test test) {
        rxPaper.write(BOOK_TESTS, test.getInfo().getTestId(), test);
    }

    public Observable<List<Test>> getTestsObservable() {
        Observable<Map<String, RxPaper.PaperObject<Test>>> observable = rxPaper.read(BOOK_TESTS);
        return observable.filter(m -> m != null).map(Map::values).map(col -> {
            List<Test> tests = new ArrayList<Test>();
            for (RxPaper.PaperObject<Test> paperObject : col) {
                if (paperObject.getChangesType() != RxPaper.ChangesType.REMOVED) {
                    tests.add(paperObject.getObject());
                }
            }
            return tests;
        });
    }

    public Observable<Test> getTestObservable(String testId) {
        Observable<RxPaper.PaperObject<Test>> observable = rxPaper.read(BOOK_TESTS, testId);
        return getValuesObservable(observable);
    }

    @NonNull
    private <T> Observable<T> getValuesObservable(Observable<RxPaper.PaperObject<T>> observable) {
        return observable.map(po -> (po != null && po.getChangesType() != RxPaper.ChangesType.REMOVED) ? po.getObject() : null);
    }

    public void removeTest(String testId) {
        rxPaper.delete(BOOK_TESTS, testId);
    }

    public boolean hasResult(String testId) {
        return rxPaper.hasKey(BOOK_RESULTS, testId);
    }

    public void setResult(String testId, List<Integer> result) {
        rxPaper.write(BOOK_RESULTS, testId, result);
    }

    public List<Integer> getResult(String testId) {
        if (hasResult(testId)) {
            RxPaper.PaperObject<List<Integer>> paperObject = rxPaper.readOnce(BOOK_RESULTS, testId);
            return paperObject.getObject();
        }
        return null;
    }

    public String getTestOfDayId() {
        RxPaper.PaperObject<String> paperObject = RxPaper.getInstance().readOnce(BOOK_DATA, TEST_OF_DAY);
        if (paperObject != null && DateUtils.isToday(paperObject.getUpdatedAt()))
            return paperObject.getObject();
        else {
            String testOfDay = null;

            Map<String, RxPaper.PaperObject<Test>> tests = RxPaper.getInstance().readOnce(BOOK_TESTS);
            if (tests != null && tests.size() > 0) {
                RxPaper.PaperObject<Test> lastTest = Collections.max(tests.values(), (lhs, rhs) -> {
                    long dateAdd1 = lhs.getObject().getInfo().getDateAdd();
                    long dateAdd2 = rhs.getObject().getInfo().getDateAdd();

                    boolean hasRes1 = hasResult(lhs.getKey());
                    boolean hasRes2 = hasResult(rhs.getKey());
                    if (!hasRes1 && !hasRes2) {
                        if (dateAdd1 > dateAdd2)
                            return 1;
                        else if (dateAdd1 < dateAdd2)
                            return -1;
                        else return 0;
                    } else if (!hasRes1) {
                        return 1;
                    } else if (!hasRes2)
                        return -1;
                    else return 0;

                });

                testOfDay = lastTest.getKey();
                RxPaper.getInstance().write(BOOK_DATA, TEST_OF_DAY, testOfDay);
            }

            return testOfDay;
        }
    }

    public void setLikeStatus(String id, boolean like) {
        rxPaper.write(BOOK_LIKE_STATUS, id, like);
    }

    //Like status can be True, False or Null
    public Observable<Boolean> getLikeStatusObservable(String id) {
        Observable<RxPaper.PaperObject<Boolean>> observable = rxPaper.read(BOOK_LIKE_STATUS, id);
        return getValuesObservable(observable);
    }

    public Observable<Map<String, RxPaper.PaperObject<Boolean>>> getLikeStatusObservable() {
        return rxPaper.read(BOOK_LIKE_STATUS);
    }

    public Observable<Map<String, RxPaper.PaperObject<Object>>> getResultsObservable() {
        return rxPaper.read(BOOK_RESULTS);
    }

    public Boolean getLikeStatus(String id) {
        RxPaper.PaperObject<Boolean> obj = rxPaper.readOnce(BOOK_LIKE_STATUS, id);
        return (obj != null && obj.getChangesType() != RxPaper.ChangesType.REMOVED) ? obj.getObject() : null;
    }

    public void updateTestLikeCounters(String testId, int countLike, int countDislike) {
        RxPaper.PaperObject<Test> obj = rxPaper.readOnce(BOOK_TESTS, testId);
        if (obj != null) {
            Test test = obj.getObject();
            test.getInfo().setLikeCount(countLike);
            test.getInfo().setDislikeCount(countDislike);

            addTest(test);
        }
    }

    public void removeLikeStatus(String testId) {
        rxPaper.delete(BOOK_LIKE_STATUS, testId);
    }

    public Observable<Boolean> getTestDoneObservable(String testId) {

        return rxPaper.read(BOOK_RESULTS, testId).map(obj -> obj != null && obj.getChangesType() != RxPaper.ChangesType.REMOVED);
    }


    public Test getTest(String id) {
        RxPaper.PaperObject<Test> obj = rxPaper.readOnce(BOOK_TESTS, id);
        return obj != null && obj.getChangesType() != RxPaper.ChangesType.REMOVED ? obj.getObject() : null;
    }
}
