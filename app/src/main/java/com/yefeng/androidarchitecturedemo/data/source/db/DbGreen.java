package com.yefeng.androidarchitecturedemo.data.source.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.DaoMaster;
import com.yefeng.androidarchitecturedemo.data.model.book.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by yefeng on 07/02/2017.
 * green db
 */

public class DbGreen {
    private static DaoSession mDaoSession;

    private DbGreen() {
    }

    private static class SingletonHolder {
        private static final DbGreen INSTANCE = new DbGreen();
    }

    public static DbGreen getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        init(context, "db");
    }

    public void init(@NonNull Context context, @NonNull String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), dbName);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            throw new NullPointerException("green db has not been initialized");
        }
        return mDaoSession;
    }
}
