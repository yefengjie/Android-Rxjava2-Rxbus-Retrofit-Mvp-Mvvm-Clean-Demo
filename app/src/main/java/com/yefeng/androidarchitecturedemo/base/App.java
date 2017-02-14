package com.yefeng.androidarchitecturedemo.base;

import android.app.Application;

/**
 * Created by yefeng on 06/02/2017.
 * app
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this);
    }
}
