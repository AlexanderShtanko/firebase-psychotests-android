package com.alexandershtanko.psychotests.viewmodels;

import android.support.v4.util.Pair;
import android.support.v7.util.SortedList;

import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.views.adapters.SortedCallback;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsViewModel extends AbstractViewModel {
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private final SortedCallback callback;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testsRef = database.getReference("tests");
    private SortedList<TestInfo> sortedList;
    private ChildEventListener childEventListener;
    private BehaviorSubject<Pair<ChildAction, DataSnapshot>> dataSubject = BehaviorSubject.create();
    private BehaviorSubject<Throwable> errorSubject = BehaviorSubject.create();

    public TestsViewModel() {
        callback = new SortedCallback();
        sortedList = new SortedList<>(TestInfo.class, callback);
    }

    @Override
    protected void onSubscribe(CompositeSubscription s) {

        s.add(Observable.create((Observable.OnSubscribe<Pair<ChildAction, DataSnapshot>>) subscriber ->
        {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    subscriber.onNext(new Pair<>(ChildAction.ACTION_ADDED, dataSnapshot));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    subscriber.onNext(new Pair<>(ChildAction.ACTION_ADDED, dataSnapshot));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    subscriber.onNext(new Pair<>(ChildAction.ACTION_ADDED, dataSnapshot));
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    subscriber.onNext(new Pair<>(ChildAction.ACTION_ADDED, dataSnapshot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());

                }
            };

            testsRef.addChildEventListener(childEventListener);
        })
                .doOnUnsubscribe(() -> testsRef.removeEventListener(childEventListener))
                .subscribe(pair -> dataSubject.onNext(pair),this::onError));


        s.add(getChildActionObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(pair -> observeData(pair.first, pair.second), ErrorUtils.onError()));
    }


    public void onError(Throwable throwable)
    {
        errorSubject.onNext(throwable);
    }

    public void observeData(ChildAction action, DataSnapshot snapshot) {
        try {
            Test test = snapshot.getValue(Test.class);
            TestInfo testInfo = test.getInfo();
            testInfo.setTestId(snapshot.getKey());

            switch (action) {
                case ACTION_ADDED:
                    sortedList.add(testInfo);
                    break;
                case ACTION_CHANGED:
                    break;
                case ACTION_MOVED:
                    break;
                case ACTION_REMOVED:
                    sortedList.remove(testInfo);
                    break;
            }
        }
        catch (Exception ignored)
        {}
    }


    public Observable<Pair<ChildAction, DataSnapshot>> getChildActionObservable() {
        return dataSubject.asObservable();
    }

    public Observable<Throwable> getErrorObservable()
    {
        return errorSubject.asObservable();
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

    private enum ChildAction {ACTION_ADDED, ACTION_CHANGED, ACTION_REMOVED, ACTION_MOVED}


}
