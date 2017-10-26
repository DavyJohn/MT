package com.zzh.mt.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 腾翔信息 on 2017/10/23.
 */

public class MyViewPage extends ViewPager {
    /*
   更改scrollble的值可设置是否可滑动，默认true为可滑动
    */
    private boolean scrollble = false;

    private int startX;
    private int startY;

    public MyViewPage(Context context)
    {
        super(context);
    }

    public MyViewPage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (!scrollble)
        {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

//                int dX = (int) (ev.getX() - startX);
                int dY = (int) (ev.getY() - startX);
                if (Math.abs(dY)>0)  // 说明上下方向滑动了
                {
                    return false;
                } else
                {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean isScrollble()
    {
        return scrollble;
    }

    public void setScrollble(boolean scrollble)
    {
        this.scrollble = scrollble;
    }
}
