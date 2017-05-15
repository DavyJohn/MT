package com.zzh.mt.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 屏幕像素转换工具类
 */
public class DisplayUtil {
  public static int getScreenWidthPx(Context context){
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metric = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(metric);
    return metric.widthPixels;
  }

  public static int getScreentWidthDp(Context context){
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metric = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(metric);
    int pxValue = metric.widthPixels;
    float scale = metric.density;
    return (int)(pxValue/scale + 0.5f);
  }

  public static int getScreenHeightDp(Context context){
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metric = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(metric);
    int pxValue = metric.heightPixels;
    float scale = metric.density;
    return (int)(pxValue/scale + 0.5f);
  }

  public static int dip2px(Context context, float dipValue){
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int)(dipValue * scale + 0.5f);
  }
  public static int px2dip(Context context, float pxValue){
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int)(pxValue / scale + 0.5f);
  }
}
