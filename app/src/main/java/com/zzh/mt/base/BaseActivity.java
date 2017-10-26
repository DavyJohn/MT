package com.zzh.mt.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.zzh.mt.R;
import com.zzh.mt.activity.HomeActivity;
import com.zzh.mt.activity.LoginActivity;
import com.zzh.mt.http.OkHttpHelper;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.LocaleUtils;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.ObserverUtils;
import com.zzh.mt.utils.SharedPreferencesUtil;

import org.xutils.x;

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
    private android.support.v7.app.AlertDialog dialog;
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
//        changeAppLanguage();
        SharedPreferencesUtil.getInstance(mContext).init(mContext);
        switchLanguage(SharedPreferencesUtil.getInstance(mContext).getString("language"));
        ObserverUtils.getInstance().addObserver(this);
        //xutils
        x.view().inject(this);
    }

    protected void switchLanguage(String language){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en")){
            configuration.locale =  Locale.ENGLISH;
            Contants.LANGUAGENEM = 1;
        }else {
            configuration.locale = Locale.SIMPLIFIED_CHINESE;Contants.LANGUAGENEM = 0;

        }
        resources.updateConfiguration(configuration,dm);
        SharedPreferencesUtil.getInstance(mContext).putString("language",language);
    }
    public void changeAppLanguage() {
//        SharedPreferences preferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
//        String sta = preferences.getString("lang", "zh");//这是SharedPreferences工具类，用于保存设置，代码很简单，自己实现吧

        Locale myLocale = LocaleUtils.getUserLocale(mContext);
        // 本地语言设置
//        if (sta.equals("zh")){
//            Contants.LANGUAGENEM = 0;
//        }else if (sta.equals("en")){
//            Contants.LANGUAGENEM = 1;
//        }
//        Locale myLocale = new Locale(sta);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(myLocale);
        }else {
            conf.locale =myLocale;
        }
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void update(Observable o, Object arg) {
//        if (arg instanceof Integer) {
//            changeAppLanguage();
//            recreate();
//        }
//        changeAppLanguage();

        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        recreate();
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
                .setTitle("提示")
                .setMessage(str)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).create();
        dialog.show();
    }
    protected void goBack(String str, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtil.getInstance(mContext).putString("userid","");
                        startActivity(new Intent(mContext,LoginActivity.class));
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
    private void logout(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.LOGOUT, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200")){
                    SharedPreferencesUtil.getInstance(mContext).putString("userid","");
                    Intent intent = new Intent(mContext,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                            Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    //给 edittext 设置晃动
    public void setShakeAnimation(View view) {
        view.startAnimation(shakeAnimation(3));
    }
    /**
     * 晃动动画
     * @param counts 半秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }
}
