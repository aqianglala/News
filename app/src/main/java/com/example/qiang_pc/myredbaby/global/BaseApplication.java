package com.example.qiang_pc.myredbaby.global;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/1/26.
 */
public class BaseApplication extends Application{

    private static BaseApplication sInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initOkHttpUtils();

    }

    private void initOkHttpUtils() {
        OkHttpUtils instance = OkHttpUtils.getInstance();
        instance.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }
}
