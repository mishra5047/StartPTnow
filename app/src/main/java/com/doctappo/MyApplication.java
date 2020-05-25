package com.doctappo;

import android.content.res.Configuration;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

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
