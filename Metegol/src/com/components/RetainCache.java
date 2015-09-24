package com.components;

import android.app.ActivityManager;
import android.content.Context;

public class RetainCache {
    private static RetainCache sSingleton;
    private Cache mRetainedCache;

    private RetainCache(Context context) {
        final int memClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;

        mRetainedCache = new Cache(size);
    }

    private static void createInstance(Context context) {
        if (sSingleton == null)
            synchronized (RetainCache.class) {
                if (sSingleton == null)
                    sSingleton = new RetainCache(context);
            }
    }

    public static RetainCache getInstance(Context context) {
        createInstance(context);
        return sSingleton;
    }

    public Cache getmCache() {
        return mRetainedCache;
    }

    public void setmRetainedCache(Cache mRetainedCache) {
        this.mRetainedCache = mRetainedCache;
    }

}
