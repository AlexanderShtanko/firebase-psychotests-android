package com.alexandershtanko.psychotests.models;

/**
 * Created by aleksandr on 13.06.16.
 */
public class TestInfo {
    private String name;
    private String desc;
    private String image;
    private String category;
    private String testId;

    private Integer likeCount;
    private Integer dislikeCount;

    private boolean done;
    private long dateAdd = 0;
    private boolean testOfDay;

    public int getLikeCount() {
        return likeCount != null ? likeCount : 0;
    }

    public int getDislikeCount() {
        return dislikeCount != null ? dislikeCount : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public long getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(long dateAdd) {
        this.dateAdd = dateAdd;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isTestOfDay() {
        return testOfDay;
    }

    public void setTestOfDay(boolean testOfDay) {
        this.testOfDay = testOfDay;
    }


    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
