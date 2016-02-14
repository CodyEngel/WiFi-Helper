package io.refact.wifihelper;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by cody on 11/28/15.
 */
public class WiFiHelperApplication extends com.activeandroid.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
