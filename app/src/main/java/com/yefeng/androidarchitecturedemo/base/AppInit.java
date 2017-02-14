package com.yefeng.androidarchitecturedemo.base;

import android.app.Application;

import com.yefeng.androidarchitecturedemo.BuildConfig;
import com.yefeng.androidarchitecturedemo.data.source.db.DbGreen;
import com.yefeng.support.base.AppInfo;
import com.yefeng.support.http.Http;
import com.yefeng.support.http.HttpCommonParams;

import timber.log.Timber;

/**
 * Created by yefeng on 07/02/2017.
 * app initialize
 */

public class AppInit {

    private static final String LOCAL_HOST = "http://192.168.20.189:8000";

    public static void init(Application app) {
        // init log
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // init app info
        AppInfo.init(app);

        // set http
        Http.setTimeOut(30);
        Http.setHost(LOCAL_HOST, Http.DEFAULT_API_VERSION);

        // init http common params
        new HttpCommonParams.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addParam("appVersion", AppInfo.appVersion)
                .addParam("appName", AppInfo.appName);

        // init green db
        DbGreen.getInstance().init(app);
    }
}
