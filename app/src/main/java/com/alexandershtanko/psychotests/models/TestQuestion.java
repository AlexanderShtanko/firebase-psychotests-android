package com.alexandershtanko.psychotests.models;

import java.util.List;

/**
 * Created by aleksandr on 13.06.16.
 */
public class TestQuestion {
    private String text;
    private Image image;
    private List<AnswerVariant> variants;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<AnswerVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<AnswerVariant> variants) {
        this.variants = variants;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
