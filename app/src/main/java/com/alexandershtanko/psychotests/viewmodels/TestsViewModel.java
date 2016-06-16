package com.alexandershtanko.psychotests.viewmodels;

import android.support.v4.util.Pair;

import com.alexandershtanko.psychotests.vvm.AbstractViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsViewModel extends AbstractViewModel {
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testsRef = database.getReference("tests");


    private ChildEventListener childEventListener;
    private BehaviorSubject<Pair<ChildAction, DataSnapshot>> dataSubject = BehaviorSubject.create();

    public TestsViewModel() {
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
                .subscribe(pair -> dataSubject.onNext(pair)));
    }


    public Observable<Pair<ChildAction,DataSnapshot>> getChildActionObservable()
    {
        return dataSubject.asObservable();
    }


    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    private enum ChildAction {ACTION_ADDED, ACTION_CHANGED, ACTION_REMOVED, ACTION_MOVED}


}
