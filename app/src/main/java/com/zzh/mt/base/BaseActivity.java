package com.zzh.mt.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.zzh.mt.R;
import com.zzh.mt.http.OkHttpHelper;
import com.zzh.mt.utils.ObserverUtils;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;


/**
 * Created by zhailiangrong on 16/6/8.
 */
public abstract class BaseActivity extends AppCompatActivity implements Observer {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private Toolbar mToolBar;
    private RelativeLayout mNoContentLayout;
    private RelativeLayout mContentLayout;
    private ToolBarX mToolBarX;
    /**
     * 提示消息
     */
    private Toast toast;
    /**
     *
     */
    protected OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstace();
    protected Context mContext;
    protected int screenwidth;
    protected int densityDpi;
//    protected WaitProgressDialog mWaitProgressDialog;
    private AlertDialog dialog;
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_baselayout);
        mContext = this;
        initView();
        View view = getLayoutInflater().inflate(getLayoutId(), mContentLayout, false); //IOC 控制反转：在父类中调用子类的反转
        mContentLayout.addView(view);
        ButterKnife.bind(this);
        unbinder = ButterKnife.bind(this);
        changeAppLanguage();
        ObserverUtils.getInstance().addObserver(this);
    }

    public void changeAppLanguage() {
        SharedPreferences preferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
        String sta = preferences.getString("lang", "zh");//这是SharedPreferences工具类，用于保存设置，代码很简单，自己实现吧
        // 本地语言设置
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            changeAppLanguage();
            recreate();
        }
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
//        mToolBar.setOverflowIcon(ContextCompat.getDrawable(mContext,R.drawable.assistant_icon));
        mContentLayout = (RelativeLayout) findViewById(R.id.content);
        mNoContentLayout = (RelativeLayout) findViewById(R.id.no_content);
        // 获取手机分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenwidth = dm.widthPixels;
        densityDpi = dm.densityDpi;
    }

    public abstract int getLayoutId();
//如何选择toolbar
    public ToolBarX getToolBar() {
        if (null == mToolBarX) {
            mToolBarX = new ToolBarX(mToolBar, this);
        }
        return mToolBarX;
    }

    public void setToolbarColor(int resid){
        mToolBar.setBackgroundColor(resid);
    }

    public void hasToolBar(boolean flag) {
        if (!flag) {
            mToolBar.setVisibility(View.GONE);
        } else {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        finish();
        overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
    }

    public void finishAndTransition(Boolean flag) {
        super.finish();
        if (flag) {
            overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
        }

    }

    /**
     * 展示Toast消息。
     *
     * @param message
     *            消息内容
     */
    public synchronized void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        }
        if (!TextUtils.isEmpty(message)) {
            toast.setText(message);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAndTransition(true);
    }

    protected void showMessageDialog(String str, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setMessage(str).setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }
    //退出
    protected void quite(String str, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setMessage(str).setTitle("提示")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出接口

                    }
                }).create();
        dialog.show();
    }

    protected void dismissMessageDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showNoContent() {
        mNoContentLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
    }

    public void dimssNoContent() {
        mNoContentLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverUtils.getInstance().deleteObserver(this);
        unbinder.unbind();
    }

}
