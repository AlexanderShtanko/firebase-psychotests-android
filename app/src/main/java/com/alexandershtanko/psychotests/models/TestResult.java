package com.alexandershtanko.psychotests.models;

/**
 * Created by aleksandr on 13.06.16.
 */
public class TestResult {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    private String text;
    private String image;
    private String from;
    private String to;
}
