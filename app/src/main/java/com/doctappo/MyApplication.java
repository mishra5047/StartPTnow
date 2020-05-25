package com.doctappo;

import android.app.Application;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import util.CommonClass;

/**
 * Created by LENOVO on 8/18/2016.
 */
public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();

        CommonClass.initRTL(this, CommonClass.getLanguage(this));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        CommonClass.initRTL(this, CommonClass.getLanguage(this));
        super.onConfigurationChanged(newConfig);
    }

}
