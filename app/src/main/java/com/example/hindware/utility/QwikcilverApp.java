package com.example.hindware.utility;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import java.lang.ref.WeakReference;

/**
 * Created by SandeepY on 07122020
 **/

public class QwikcilverApp extends MultiDexApplication {
    private static WeakReference<QwikcilverApp> wApp = new WeakReference<>(null);

    public static QwikcilverApp getInstance() {
        return wApp.get();
    }

    public static Context getContext() {
        QwikcilverApp app = wApp.get();
        return app != null ? app.getApplicationContext() : new QwikcilverApp().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wApp.clear();
        wApp = new WeakReference<>(this);
        // FirebaseApp.initializeApp(this);
        QwikcilverAPI.setAPIEnvironment();
    }
}
