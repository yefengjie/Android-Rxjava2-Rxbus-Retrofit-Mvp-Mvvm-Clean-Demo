package com.yefeng.androidarchitecturedemo.base;

import android.app.Application;

import com.yefeng.androidarchitecturedemo.BuildConfig;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.source.db.DbGreen;
import com.yefeng.support.http.Http;
import com.yefeng.support.http.HttpCommonParams;

import timber.log.Timber;

/**
 * Created by yefeng on 07/02/2017.
 * app initialize
 */

public class AppInit {

    public static void init(Application app) {
        // init log
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // set http
        Http.setTimeOut(30);
        Http.setHost(Http.DEFAULT_HOST, Http.DEFAULT_API_VERSION);

        // init http common params
        new HttpCommonParams.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addParam("appVersion", "1.0.0")
                .addParam("appName", app.getString(R.string.app_name));

        // init green db
        DbGreen.getInstance().init(app);
    }
}
