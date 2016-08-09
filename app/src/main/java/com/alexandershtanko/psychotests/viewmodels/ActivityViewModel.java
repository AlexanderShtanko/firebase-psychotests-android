package com.alexandershtanko.psychotests.viewmodels;

import android.content.Context;

import com.alexandershtanko.psychotests.helpers.AlarmHelper;
import com.alexandershtanko.psychotests.models.Storage;
import com.alexandershtanko.psychotests.models.Test;
import com.alexandershtanko.psychotests.utils.DeviceUtils;
import com.alexandershtanko.psychotests.utils.RxFirebase;
import com.alexandershtanko.psychotests.utils.RxPaper;
import com.alexandershtanko.psychotests.vvm.AbstractViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class ActivityViewModel extends AbstractViewModel {
    private static final int ALARM_ID = 111;
    public static final String TESTS = "tests";
    public static final String LIKE_STATUS = "like_status";
    private final String deviceId;
    private Storage storage;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public ActivityViewModel(Context context) {
        deviceId = DeviceUtils.getDeviceId(context);
        storage = Storage.getInstance();
        storage.init(context);
        AlarmHelper.setTestOfDayAlarm(context);
    }


    @Override
    protected void onSubscribe(CompositeSubscription s) {
        s.add(RxFirebase.getChildrenObservable(databaseReference.child(TESTS), Test.class)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(this::onError)
                .subscribe(this::observe, this::onError));

        s.add(storage.getLikeStatusObservable().skip(1)
                .subscribeOn(Schedulers.io())
                .doOnError(this::onError)
                .subscribe(this::sendTestLikeStatus, this::onError));

    }

    private void sendTestLikeStatus(Map<String, RxPaper.PaperObject<Boolean>> objectMap) {
        if (objectMap != null)
            for (RxPaper.PaperObject<Boolean> object : objectMap.values()) {
                if (object.getChangesType() == RxPaper.ChangesType.REMOVED) {
                    databaseReference.child(LIKE_STATUS).child(object.getKey()).child(deviceId).removeValue();
                } else {
                    databaseReference.child(LIKE_STATUS).child(object.getKey()).child(deviceId).setValue(object.getObject());
                }
            }
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

                databaseReference.child(LIKE_STATUS).child(testId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();

                        int countLike = 0;
                        int countDislike = 0;
                        if (map != null)
                            for (Boolean like : map.values()) {
                                if (like)
                                    countLike++;
                                if (!like)
                                    countDislike++;
                            }

                        test.getInfo().setLikeCount(countLike);
                        test.getInfo().setDislikeCount(countDislike);

                        storage.addTest(test);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
