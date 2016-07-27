package com.alexandershtanko.psychotests.utils;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by aleksandr on 13.07.16.
 */
public class StringUtils {
    public static String capitalizeFirstLetter(String text) {
        if(text!=null) {
            return WordUtils.capitalizeFully(text.toLowerCase(), '.', '?', '!');
        }
        return null;
    }
}
