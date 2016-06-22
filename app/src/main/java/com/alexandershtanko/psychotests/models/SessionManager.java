package com.alexandershtanko.psychotests.models;

import java.util.List;

/**
 * Created by aleksandr on 19.06.16.
 */
public class SessionManager {
    private static SessionManager instance;
    private Test selectedTest;
    private List<Integer> result;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public void setTest(Test test)
    {
        this.selectedTest = test;
    }

    public Test getTest()
    {
        return selectedTest;
    }

    public void setResult(List<Integer> result) {
        this.result = result;
    }


    public List<Integer> getResult() {
        return result;
    }
}
