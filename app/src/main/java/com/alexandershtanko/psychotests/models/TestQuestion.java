package com.alexandershtanko.psychotests.models;

import java.util.List;

/**
 * Created by aleksandr on 13.06.16.
 */
public class TestQuestion {
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

    private String text;
    private List<AnswerVariant> variants;
}
