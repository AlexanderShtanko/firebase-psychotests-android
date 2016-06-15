package com.alexandershtanko.psychotests.viewmodels;

import android.support.v4.util.Pair;
import android.support.v7.util.SortedList;

import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;
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


    private SortedList<TestInfo> sortedList;
    private ChildEventListener childEventListener;

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
                .subscribe(pair -> obtainData(pair.first, pair.second)));
    }

    private void obtainData(ChildAction action, DataSnapshot dataSnapshot) {
        Test test = dataSnapshot.getValue(Test.class);

        switch (action) {
            case ACTION_ADDED:
                sortedList.add(test.getInfo());
                break;
            case ACTION_CHANGED:
                sortedList.add(test.getInfo());
                break;
            case ACTION_REMOVED:
                sortedList.remove(test.getInfo());
                break;
            case ACTION_MOVED:
                break;
        }
    }


    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    private enum ChildAction {ACTION_ADDED, ACTION_CHANGED, ACTION_REMOVED, ACTION_MOVED}


}
