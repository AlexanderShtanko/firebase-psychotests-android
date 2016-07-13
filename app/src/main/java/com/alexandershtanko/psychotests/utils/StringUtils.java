package com.alexandershtanko.psychotests.utils;

/**
 * Created by aleksandr on 13.07.16.
 */
public class StringUtils {
    public static String capitalizeFirstLetter(String text) {
        if (text != null && text.length() > 0)
            text = String.valueOf(text.charAt(0)).toUpperCase() + text.subSequence(1, text.length());
        return text;
    }
}
