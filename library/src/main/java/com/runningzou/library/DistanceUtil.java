package com.runningzou.library;

import android.content.res.Resources;

/**
 * Created by runningzou on 2017/9/8.
 */

public class DistanceUtil {

    private static float density = Resources.getSystem().getDisplayMetrics().density;

    public static float dp2px(float dpValue) {

        return dpValue * density;
    }

    public static int dp2px(int dpValue) {
        //四舍五入
        return (int) (dpValue * density + 0.5f);
    }


    public static float px2dp(float pxValue) {
        return pxValue / density + 0.5f;
    }

    public static int px2dp(int pxValue) {
        //四舍五入
        return (int) (pxValue / density + 0.5f);
    }
}
