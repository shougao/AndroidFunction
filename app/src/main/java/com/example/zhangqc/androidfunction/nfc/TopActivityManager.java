package com.example.zhangqc.androidfunction.nfc;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by zhangqc8 on 2017/1/22.
 */

public class TopActivityManager {

    private static TopActivityManager mInstance;
    private WeakReference<Activity> mCurrentActivityWeakRef;

    private TopActivityManager(){

    }

    public static TopActivityManager getInstance() {
        if(mInstance == null){
            mInstance = new TopActivityManager();
        }
        return mInstance;
    }

    public Activity getCurrentActivity(){
        if(mCurrentActivityWeakRef == null){
            return null;
        }
        return mCurrentActivityWeakRef.get();
    }

    public void setCurrentActivity(Activity topActivity){
        mCurrentActivityWeakRef = new WeakReference<Activity>(topActivity);
    }

}
