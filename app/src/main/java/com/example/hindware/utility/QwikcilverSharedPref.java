package com.example.hindware.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SandeepY on 08122020
 **/

public class QwikcilverSharedPref {

    private static final String TAG = QwikcilverSharedPref.class.getSimpleName();
    private static String SHARED_PREF = "QwikcilverSharedPref";

    public static void setStringValue(SharedPreferences sharedPreferences, String key, String value) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(SharedPreferences sharedPreferences, String key) {
        try {
            return sharedPreferences.getString(key, "");
        } catch (Exception e) {
            return "";
        }
    }

    public static void setIntValue(SharedPreferences sharedPreferences, String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntValue(SharedPreferences sharedPreferences, String key) {
        try {
            return sharedPreferences.getInt(key, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setBooleanValue(SharedPreferences sharedPreferences, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanValue(SharedPreferences sharedPreferences, String key) {
        try {
            return sharedPreferences.getBoolean(key, false);
        } catch (Exception e) {
            return false;
        }
    }

    public static void setLongValue(SharedPreferences sharedPreferences, String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongValue(SharedPreferences sharedPreferences, String key) {
        try {
            return sharedPreferences.getLong(key, (long) 0.0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setFloatValue(SharedPreferences sharedPreferences, String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloatValue(SharedPreferences sharedPreferences, String key) {
        try {
            return sharedPreferences.getFloat(key, (float) 0.0);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clearAllData(SharedPreferences sharedPreferences) {
        sharedPreferences.edit().clear().apply();
    }

    public SharedPreferences getQwikcilverSharedPref() {
        return QwikcilverApp.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
    }
}
