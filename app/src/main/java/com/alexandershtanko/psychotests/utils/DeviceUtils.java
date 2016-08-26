package com.alexandershtanko.psychotests.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Patterns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by aleksandr on 24.02.16.
 */
public class DeviceUtils {
    public static String getDeviceId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String getPhoneNumber(Context context) {
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tMgr.getLine1Number();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getPrimaryEmail(Context context)
    {
        try {
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(context).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    return account.name;
                }
            }
        }
        catch (Throwable ignored){}
        return null;
    }

    public static List<String> getInstalledPackageNameList(Context context)
    {
        List<String> packageNameList=new ArrayList<>();

        try {
            final PackageManager pm = context.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            if (packages != null) {
                for (ApplicationInfo appInfo : packages) {
                    if (!isSystemPackage(appInfo)) {
                        packageNameList.add(appInfo.packageName);
                    }
                }
            }
        }
        catch (Exception ignored){}

        return packageNameList;
    }

    private static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static int megabytesAvailable() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (int) (availableBlocks*blockSize/1024f/1024f);

    }
}
