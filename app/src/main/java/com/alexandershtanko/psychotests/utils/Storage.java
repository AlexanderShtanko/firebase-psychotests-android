package com.alexandershtanko.psychotests.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandr on 03.07.16.
 */
public class Storage {

    public static final String PREFS_TEST_RESULTS = "test-results";
    private final Context context;
    private final SharedPreferences testResults;

    public Storage(Context context) {
        this.context = context;
        testResults = context.getSharedPreferences(PREFS_TEST_RESULTS, Context.MODE_PRIVATE);

    }

    public void saveResult(String testId, List<Integer> result) {
        testResults.edit().putString(testId, StringUtils.join(result, ',')).apply();
    }

    public boolean hasResult(String testId) {
        return testResults.contains(testId);
    }

    public void removeResult(String testId) {
        if (hasResult(testId))
            testResults.edit().remove(testId).apply();
    }

    public List<Integer> getResult(String testId) {
        if (hasResult(testId)) {
            String string = testResults.getString(testId, null);
            if (string != null) {
                String[] array = string.split(",");
                List<Integer> result = new ArrayList<>();
                for (String res : array) {
                    result.add(Integer.parseInt(res));
                }
                return result;
            } else return null;
        } else return null;
    }
}
