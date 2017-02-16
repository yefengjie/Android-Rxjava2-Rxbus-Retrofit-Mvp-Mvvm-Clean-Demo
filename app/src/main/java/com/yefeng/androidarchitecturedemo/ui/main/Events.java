package com.yefeng.androidarchitecturedemo.ui.main;

/**
 * Created by yefeng on 16/02/2017.
 */

public class Events {

    private Events() {
    }

    public static class ReloadEvent {
        boolean mForceUpdate;

        public ReloadEvent(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }
    }

}
