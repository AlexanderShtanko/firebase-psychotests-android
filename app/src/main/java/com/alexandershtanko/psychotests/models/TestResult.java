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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    private String text;
    private Image image;
    private Integer from;
    private Integer to;
}
