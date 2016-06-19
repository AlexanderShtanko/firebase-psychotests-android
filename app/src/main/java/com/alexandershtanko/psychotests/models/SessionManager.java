package com.alexandershtanko.psychotests.models;

/**
 * Created by aleksandr on 19.06.16.
 */
public class SessionManager {
    private static SessionManager instance;
    private Test selectedTest;
    private Integer result;

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

    public void setResult(Integer result) {
        this.result = result;
    }


    public Integer getResult() {
        return result;
    }
}
