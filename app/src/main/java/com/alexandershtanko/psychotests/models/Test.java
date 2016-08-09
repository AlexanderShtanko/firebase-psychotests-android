package com.alexandershtanko.psychotests.models;


import java.util.List;

/**
 * Created by aleksandr on 13.06.16.
 */
public class Test {
    private TestInfo info;
    private List<TestQuestion> questions;
    private List<TestResult> results;



    public TestInfo getInfo() {
        return info;
    }

    public void setInfo(TestInfo info) {
        this.info = info;
    }

    public List<TestQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TestQuestion> questions) {
        this.questions = questions;
    }

    public List<TestResult> getResults() {
        return results;
    }

    public void setResults(List<TestResult> results) {
        this.results = results;
    }

}
