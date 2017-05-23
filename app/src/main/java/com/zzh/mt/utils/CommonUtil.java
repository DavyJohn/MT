package com.zzh.mt.utils;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;
import android.widget.Toast;

import com.zzh.mt.base.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 腾翔信息 on 2017/5/17.
 */

public class CommonUtil {
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    public static void exitBy2Click(Context context) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(context, "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT)
                    .show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            MyApplication.getInstance().finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    	public static void moveCursor2End(EditText editText) {
        if (editText.hasFocus()) {
            Editable text = editText.getText();
            int position = text.length();
            Selection.setSelection(text, position);
        }
    }
}
