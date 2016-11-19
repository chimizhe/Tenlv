package com.tenglv.gate;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by minhengYan on 2016/11/15.
 * email：minheng_yan@163.com
 */

public class TenLvApplication extends MultiDexApplication {
    public static String sToken = "";
    private static TenLvApplication mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static TenLvApplication getInstance(){
        return mInstance;
    }
}
