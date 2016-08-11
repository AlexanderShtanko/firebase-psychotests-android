package com.alexandershtanko.psychotests.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by alexander on 05/07/16.
 */
public class RxPaper {
    private static RxPaper instance;
    Map<String, Map<String, PublishSubject<PaperObject>>> keyUpdatesSubjectMap = new HashMap<>();
    Map<String, PublishSubject<Map<String, PaperObject>>> bookUpdatesSubjectMap = new HashMap<>();

    public static synchronized RxPaper getInstance() {
        if (instance == null)
            instance = new RxPaper();
        return instance;
    }

    public void init(Context context) {
        Paper.init(context);
    }

    public void destroy() {
        keyUpdatesSubjectMap.clear();
        bookUpdatesSubjectMap.clear();
    }


    //==========================Single item changes===============================


    public <T> Observable<PaperObject<T>> read(String bookName, String key) {
        return Observable.create((Observable.OnSubscribe<PaperObject>) subscriber -> {
            PaperObject<T> paperObject = Paper.book(bookName).read(key);
            subscriber.onNext(paperObject);
        }).mergeWith(getKeyUpdatesSubject(bookName, key))
                .map(paperObject ->
                        (PaperObject<T>) paperObject).subscribeOn(Schedulers.io());
    }


    public <T> Observable<Map<String, PaperObject<T>>> read(String bookName) {
        return Observable.create((Observable.OnSubscribe<Map<String, PaperObject>>) subscriber -> {
            Map<String, PaperObject> map = readOnceInternal(bookName);
            subscriber.onNext(map);
        }).mergeWith(getBookUpdatesSubject(bookName).asObservable())
                .map(oldMap ->
                {
                    Map<String, PaperObject<T>> map = new HashMap<>();
                    for (String key : oldMap.keySet()) {
                        if (oldMap.get(key) != null)
                            map.put(key, oldMap.get(key));
                    }
                    return map;
                }).subscribeOn(Schedulers.io());
    }

    public <T> Map<String, PaperObject<T>> readOnce(String bookName) {
        List<String> keys = Paper.book(bookName).getAllKeys();
        Map<String, PaperObject<T>> map = new HashMap<>();
        for (String key : keys) {
            PaperObject<T> paperObject = readOnce(bookName, key);
            if (paperObject != null)
                map.put(key, paperObject);
        }
        return map;
    }

    public <T> PaperObject<T> readOnce(String bookName, String key) {
        return Paper.book(bookName).read(key);
    }


    //Call this methods in background thread
    public <T> void write(String bookName, String key, T object) {

        PaperObject<T> paperObject = Paper.book(bookName).read(key);
        if (paperObject == null)
            paperObject = new PaperObject<>(key, object);
        else
            paperObject.updateObject(object);

        Paper.book(bookName).write(key, paperObject);

        getKeyUpdatesSubject(bookName, key).onNext(paperObject);

        Map<String, PaperObject> map = new HashMap<>();
        map.put(key, paperObject);
        getBookUpdatesSubject(bookName).onNext(map);
    }


    public <T> void write(String bookName, Map<String, T> objectMap) {
        Map<String, PaperObject> map = new HashMap<>();

        for (String key : objectMap.keySet()) {
            T object = objectMap.get(key);

            PaperObject<T> paperObject = Paper.book(bookName).read(key);
            if (paperObject == null)
                paperObject = new PaperObject<>(key, object);
            else
                paperObject.updateObject(object);

            Paper.book(bookName).write(key, paperObject);

            getKeyUpdatesSubject(bookName, key).onNext(paperObject);

            map.put(key, paperObject);
        }

        getBookUpdatesSubject(bookName).onNext(map);
    }

    public void delete(String bookName, String key) {
        PaperObject paperObject = Paper.book(bookName).read(key);
        paperObject.markObjectAsRemoved();

        Paper.book(bookName).delete(key);

        getKeyUpdatesSubject(bookName, key).onNext(paperObject);

        Map<String, PaperObject> map = new HashMap<>();
        map.put(key, paperObject);
        getBookUpdatesSubject(bookName).onNext(map);
    }

    public void delete(String bookName) {
        Paper.book(bookName).destroy();
        getBookUpdatesSubject(bookName).onNext(null);
    }

    @NonNull
    private <T> Map<String, PaperObject> readOnceInternal(String bookName) {
        List<String> keys = Paper.book(bookName).getAllKeys();
        Map<String, PaperObject> map = new HashMap<>();
        for (String key : keys) {
            PaperObject<T> paperObject = Paper.book(bookName).read(key);
            map.put(key, paperObject);
        }
        return map;
    }


    private PublishSubject<PaperObject> getKeyUpdatesSubject(String bookName, String key) {
        if (!keyUpdatesSubjectMap.containsKey(bookName)) {
            keyUpdatesSubjectMap.put(bookName, new HashMap<>());
        }

        if (!keyUpdatesSubjectMap.get(bookName).containsKey(key)) {
            keyUpdatesSubjectMap.get(bookName).put(key, PublishSubject.create());
        }

        return keyUpdatesSubjectMap.get(bookName).get(key);
    }


    private PublishSubject<Map<String, PaperObject>> getBookUpdatesSubject(String bookName) {
        if (!bookUpdatesSubjectMap.containsKey(bookName)) {
            bookUpdatesSubjectMap.put(bookName, PublishSubject.create());
        }

        return bookUpdatesSubjectMap.get(bookName);
    }

    public boolean hasKey(String book, String key) {
        return Paper.book(book).exist(key);
    }

    public enum ChangesType {ADDED, UPDATED, REMOVED}


    public static class PaperObject<T> {
        private long updatedAt;
        private long createdAt;
        private ChangesType changesType;
        private T object;
        private String key;

        public PaperObject(String key, T object) {
            this.changesType = ChangesType.ADDED;
            this.key = key;
            this.object = object;
            this.updatedAt = new Date().getTime();
            this.createdAt = updatedAt;
        }

        public ChangesType getChangesType() {
            return changesType;
        }


        public T getObject() {
            return object;
        }

        public void updateObject(T object) {
            this.updatedAt = new Date().getTime();
            changesType = ChangesType.UPDATED;
            this.object = object;
        }

        public void markObjectAsRemoved() {
            this.updatedAt = new Date().getTime();
            changesType = ChangesType.REMOVED;
        }


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
    }
}
