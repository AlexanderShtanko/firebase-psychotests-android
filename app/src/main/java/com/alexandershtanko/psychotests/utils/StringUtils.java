package com.alexandershtanko.psychotests.utils;

/**
 * Created by aleksandr on 13.07.16.
 */
public class StringUtils {
    public static String capitalizeSentences(String text) {

        if (text != null && text.length() > 0) {
            text = text.toLowerCase();
            StringBuilder sb = new StringBuilder(text);
            int pos=0;
            boolean capitalize = true;
            while (pos < sb.length()) {
                if (sb.charAt(pos) == '.') {
                    capitalize = true;
                } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
                    sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
                    capitalize = false;
                }
                pos++;
            }
            text = sb.toString();
        }
        return text;
    }
}
