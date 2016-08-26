package com.alexandershtanko.psychotests.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alexandershtanko.psychotests.helpers.AlarmHelper;
import com.alexandershtanko.psychotests.helpers.AmplitudeHelper;
import com.alexandershtanko.psychotests.helpers.RemoteConfigHelper;
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
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class ActivityViewModel extends AbstractViewModel {
    private static final int ALARM_ID = 111;
    public static final String TESTS = "tests";
    public static final String LIKE_STATUS = "like_status";
    private final String deviceId;
    private final Context context;
    private final int freeSpace;
    private BehaviorSubject<DataSnapshot> likeDataSnapshotSubject = BehaviorSubject.create();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public ActivityViewModel(Activity activity) {
        this.context = activity;

        freeSpace = DeviceUtils.megabytesAvailable();

        deviceId = DeviceUtils.getDeviceId(context);
        Storage.getInstance().init(context);
        AlarmHelper.setTestOfDayAlarm(context);

        AmplitudeHelper.init(activity);
        AmplitudeHelper.onStart();
        RemoteConfigHelper.updateValues();

        Log.e("ActivityViewModel", "Show TOD notification:" + RemoteConfigHelper.isNeedShowTODNotification());
    }

    @Override
    protected void onSubscribe(CompositeSubscription s) {


        s.add(RxFirebase.getChildrenObservable(databaseReference.child(TESTS), Test.class).onBackpressureBuffer()
                .doOnError(this::onError)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::observe, this::onError));

        s.add(likeDataSnapshotSubject.asObservable().onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::obtainLikeDataSnapshot,this::onError));

        s.add(Storage.getInstance().getLikeStatusObservable().skip(1)
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
                Storage.getInstance().addTest(test);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likeDataSnapshotSubject.onNext(dataSnapshot);
                        databaseReference.child(LIKE_STATUS).child(testId).removeEventListener(this);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                databaseReference.child(LIKE_STATUS).child(testId).addListenerForSingleValueEvent(valueEventListener);
                break;

            case REMOVED:
                Storage.getInstance().removeTest(testId);
                break;
        }


    }

    private void obtainLikeDataSnapshot(DataSnapshot dataSnapshot) {
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

        String testId = dataSnapshot.getKey();
        Storage.getInstance().updateTestLikeCounters(testId, countLike, countDislike);
    }

    @Override
    public void saveInstanceState() {

    }

    @Override
    public void restoreInstanceState() {

    }

    public int getFreeSpace() {
        return freeSpace;
    }
}
