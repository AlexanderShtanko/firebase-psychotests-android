package com.alexandershtanko.psychotests.models;

/**
 * Created by aleksandr on 13.06.16.
 */
public class AnswerVariant {
    private String text;
    private Integer value;
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
