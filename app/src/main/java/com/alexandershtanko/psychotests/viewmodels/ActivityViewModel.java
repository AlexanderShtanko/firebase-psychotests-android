package com.alexandershtanko.psychotests.viewmodels;

import android.content.Context;

import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.utils.RxFirebase;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class ActivityViewModel extends AbstractViewModel {
    private Storage storage;
    private DatabaseReference testsRef = FirebaseDatabase.getInstance().getReference("tests");

    public ActivityViewModel(Context context) {
        storage = Storage.getInstance();
        storage.init(context);
    }

    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(RxFirebase.getChildrenObservable(testsRef, Test.class)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::observe, this::onError));
    }

    public void observe(RxFirebase.ChildEvent<Test> event) {
        Test test = event.getValue();
        String testId = event.getKey();
        test.getInfo().setTestId(testId);

        switch (event.getEventType()) {
            case CHANGED:
            case MOVED:
            case ADDED:
                storage.addTest(test);
                break;

            case REMOVED:
                storage.removeTest(testId);
                break;
        }
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }
}
