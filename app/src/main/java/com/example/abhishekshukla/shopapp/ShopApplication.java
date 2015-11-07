package com.example.abhishekshukla.shopapp;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

import com.example.abhishekshukla.shopapp.cache.ProductCache;
import com.example.abhishekshukla.shopapp.util.ImageLoader;


public class ShopApplication extends android.app.Application {
    public static boolean DEBUG = false;
    public static final String LOG_TAG = "ShopApp";
    private static android.app.Application sInstance = null;
    
    @Override
    public void onCreate() {
        sInstance = this;
        super.onCreate();
        DEBUG = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                        .setDefaultFontPath("fonts/Tahoma.ttf")
//                        .setFontAttrId(R.attr.fontPath)
//                        .build()
//        );
        if ( getApplicationInfo().processName.equals(getCurrentProcessName()) ) {
            initFvApplication();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    
    private void initFvApplication() {
        new Thread() {
            @Override
            public void run() {
                ImageLoader.getInstance().loadFromDiskCache();
                ProductCache.getInstance().initCache(getInstance());
            }
        }.start();
    }
    
    private String getCurrentProcessName() {
        final ActivityManager activityManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        final List<RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo process : runningProcesses) {
            if ( process.pid == android.os.Process.myPid() ) 
                return process.processName;
        }
        return null;
    }
    
    public static final android.app.Application getInstance() {
        return sInstance;
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ProductCache.getInstance().saveProductCache(getInstance());
    }
}
