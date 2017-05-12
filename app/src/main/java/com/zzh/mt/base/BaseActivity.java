package com.zzh.mt.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.fw.R;
import com.example.fw.activities.main.HomeActivity;
import com.example.fw.http.OkHttpHelper;
import com.example.fw.http.SpotsCallBack;
import com.example.fw.mode.response.BaseResponse;
import com.example.fw.utils.Constant;
import com.example.fw.utils.MdTools;
import com.example.fw.utils.SharedPreferencesUtil;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.Duration;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.util.LinkedHashMap;

import okhttp3.Response;


/**
 * Created by zhailiangrong on 16/6/8.
 */
public abstract class BaseActivity extends AppCompatActivity {

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
    protected AppUpdater appupdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_baselayout);
        mContext = this;
        initView();
        View view = getLayoutInflater().inflate(getLayoutId(), mContentLayout, false); //IOC 控制反转：在父类中调用子类的反转
        mContentLayout.addView(view);
    }

    private void initView() {
//        mWaitProgressDialog = new WaitProgressDialog(this,"正在加载中...");
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setOverflowIcon(ContextCompat.getDrawable(mContext,R.drawable.assistant_icon));
        mContentLayout = (RelativeLayout) findViewById(R.id.content);
        mNoContentLayout = (RelativeLayout) findViewById(R.id.no_content);
        // 获取手机分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenwidth = dm.widthPixels;
        densityDpi = dm.densityDpi;
        appupdater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setDuration(Duration.NORMAL)
                .setUpdateFrom(UpdateFrom.XML)
//                .setUpdateXML(Constant.UPDATEURL)
                .setDialogTitleWhenUpdateAvailable("可更新")
                .setDialogButtonUpdate("立即更新")
                .setDialogButtonDismiss("稍后再说")
                .setDialogButtonDoNotShowAgain("不再提示")
                .setDialogTitleWhenUpdateNotAvailable("无可用更新")
                .setDialogDescriptionWhenUpdateNotAvailable("您的app是最新版本！");

        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.XML)
                .withListener(new AppUpdaterUtils.AppUpdaterListener() {
                    @Override
                    public void onSuccess(String s, Boolean isUpdateAvailable) {
                        Log.d("AppUpdater", s + Boolean.toString(isUpdateAvailable));
                    }
                    @Override
                    public void onFailed(AppUpdaterError appUpdaterError) {
                        Log.d("AppUpdater", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }

    public abstract int getLayoutId();
//如何选择toolbar
    public ToolBarX getToolBar() {
        if (null == mToolBarX) {
            mToolBarX = new ToolBarX(mToolBar, this);
        }
        return mToolBarX;
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
                        logout();

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

    private void logout(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("sign", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.USERLOGOUT, map, TAG, new SpotsCallBack<BaseResponse>(mContext,false) {

            public void onSuccess(Response response, BaseResponse o) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                switch (o.getCode()){
                    case "200":
                        SharedPreferencesUtil.getInstance(mContext).putString("password","");
                        intent.putExtra("finish",true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(intent);
                        finish();
                        break;
                    case "500":
//                        showToast("退出失败");
                        break;
                    case "110":
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        break;
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //登陆界面的禁止登录
}
