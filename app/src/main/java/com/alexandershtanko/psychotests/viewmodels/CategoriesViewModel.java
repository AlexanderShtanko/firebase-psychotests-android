package com.alexandershtanko.psychotests.viewmodels;

import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class CategoriesViewModel extends AbstractViewModel {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference testsRef = database.getReference("tests");
    private BehaviorSubject<DataSnapshot> dataSubject = BehaviorSubject.create();
    private BehaviorSubject<Throwable> errorSubject = BehaviorSubject.create();
    private BehaviorSubject<Set<String>> categoriesSubject = BehaviorSubject.create(new HashSet<>());
    private BehaviorSubject<String> categorySubject = BehaviorSubject.create();


    private ChildEventListener childEventListener;

    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(Observable.create((Observable.OnSubscribe<DataSnapshot>) subscriber ->
        {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    subscriber.onNext(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    subscriber.onNext(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());

                }
            };

            testsRef.addChildEventListener(childEventListener);
        })
                .doOnUnsubscribe(() -> testsRef.removeEventListener(childEventListener))
                .subscribe(dataSnapshot -> dataSubject.onNext(dataSnapshot),this::onError));


        s.add(dataSubject.asObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(data -> observeData(data), ErrorUtils.onError()));

    }

    private void observeData(DataSnapshot data) {
        Test test = data.getValue(Test.class);
        TestInfo testInfo = test.getInfo();
        String category = testInfo.getCategory();

        categorySubject.onNext(category);

        Set<String> categories = categoriesSubject.getValue();
        categories.add(category);
        categoriesSubject.onNext(categories);
    }

    public Observable<Set<String>> getCategoriesObservable()
    {
        return categoriesSubject.asObservable().first();
    }

    public Observable<String> getCategoryObservable()
    {
        return categorySubject.asObservable();
    }

    public Observable<Throwable> getErrorObservable()
    {
        return errorSubject.asObservable();
    }


    public void onError(Throwable throwable)
    {
        errorSubject.onNext(throwable);
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }
}
