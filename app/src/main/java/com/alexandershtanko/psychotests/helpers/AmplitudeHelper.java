package com.alexandershtanko.psychotests.helpers;

import android.app.Activity;

import com.alexandershtanko.psychotests.models.TestResult;
import com.alexandershtanko.psychotests.utils.ErrorUtils;
import com.amplitude.api.Amplitude;

import org.json.JSONObject;

/**
 * Created by alexander on 17/08/16.
 */
public class AmplitudeHelper {
    private static final String TAG = AmplitudeHelper.class.getSimpleName();

    public static void testDone(String testId, String testName, String category) {
        try {
            JSONObject object = new JSONObject();
            object.put("testId", testId);
            object.put("testName", testName);
            object.put("category", category);

            Amplitude.getInstance().logEvent("Test Done", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }

    }

    public static void onOpenResult(String testId, String testName, String category, TestResult testResult) {
        try {
            JSONObject object = new JSONObject();
            object.put("testId", testId);
            object.put("testName", testName);
            object.put("category", category);

            if (testResult != null)
                object.put("testResultText", testResult.getText());
            Amplitude.getInstance().logEvent("Result Open", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onOpenTestInfo(String testId, String testName, String category, boolean hasResult,boolean testOfDay) {
        try {
            JSONObject object = new JSONObject();
            object.put("testId", testId);
            object.put("testName", testName);
            object.put("hasResult", hasResult);
            object.put("testOfDay",testOfDay);
            object.put("category", category);

            Amplitude.getInstance().logEvent("Test Info Open", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onLike(String testId, String testName, String category, boolean like) {
        try {
            JSONObject object = new JSONObject();
            object.put("testId", testId);
            object.put("testName", testName);
            object.put("like", like);
            object.put("category", category);

            Amplitude.getInstance().logEvent("Like", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onOpenTest(String testId, String testName, String category) {
        try {
            JSONObject object = new JSONObject();
            object.put("testId", testId);
            object.put("testName", testName);
            object.put("category", category);
            Amplitude.getInstance().logEvent("Test Open", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onOpenTests(String category, Boolean onlyPassed, Boolean onlyFavorite) {
        try {
            JSONObject object = new JSONObject();
            object.put("category", category);
            object.put("onlyPassed", onlyPassed);
            object.put("onlyFavorite", onlyFavorite);
            Amplitude.getInstance().logEvent("Tests Open", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onOpenCategories() {
        try {
            Amplitude.getInstance().logEvent("Categories Open");
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void init(Activity activity) {
        Amplitude.getInstance().initialize(activity, "615b40ffde66490ac80321aea1f9f4ed").enableForegroundTracking(activity.getApplication());
    }

    public static void onStart() {
        try {
            Amplitude.getInstance().logEvent("Start");
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onPushNotificationShow(String title, String body) {
        try {
            JSONObject object = new JSONObject();
            object.put("title", title);
            object.put("body", body);
            Amplitude.getInstance().logEvent("Push Notification Show", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onShowTODNotification(boolean showNotification) {
        try {
            JSONObject object = new JSONObject();
            object.put("tod_notification remote config", showNotification);
            Amplitude.getInstance().logEvent("Test of Day Notification Show",object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onRateUsResult(boolean like) {
        try {
            JSONObject object = new JSONObject();
            object.put("like", like);
            Amplitude.getInstance().logEvent("RateUs Result", object);
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }

    public static void onRateUsOpen() {
        try {
            Amplitude.getInstance().logEvent("RateUs Open");
        } catch (Exception e) {
            ErrorUtils.log(TAG, e);
        }
    }
}
