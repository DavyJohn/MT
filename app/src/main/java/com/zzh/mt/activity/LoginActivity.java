package com.zzh.mt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.LoginData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/17.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private boolean isapppwd = true;
    @BindView(R.id.login_et_username)
    EditText mEtUserName;
    @BindView(R.id.login_et_password)
    EditText mEtPassword;
    @BindView(R.id.login_eye)
    ImageView mEye;
    @BindView(R.id.login_toolbar)
    Toolbar mTool;
    @OnClick(R.id.login_registered) void registered(){
        startActivity(new Intent(mContext,RegisteredActivity.class));
    }

    @OnClick(R.id.login_forget_password) void forget(){
        startActivity(new Intent(mContext,ForgetPassActivity.class));
    }
    @OnClick(R.id.login_eye) void appearpwd(){
        isAppearPwd();
    }
    @OnClick(R.id.login) void log(){
        if (TextUtils.isEmpty(mEtUserName.getText().toString())){
            setShakeAnimation(mEtUserName);
            showToast("账号不能为空！");

        }else
//            if (!CommonUtil.isEmail(mEtUserName.getText().toString())){
//            showMessageDialog("输入的账号格式不正确！",mContext);
//        } else
            if (TextUtils.isEmpty(mEtPassword.getText().toString())){
                setShakeAnimation(mEtPassword);
                showToast("密码不能为空！");
        }
//        else if (mEtPassword.getText().toString().length()<6){
//            showMessageDialog("密码长度不小于6位！",mContext);
//        }
        else {
            login();
        }


    }

    private void isAppearPwd() {
        if (isapppwd == true) {
            // 设置为密文显示
            Picasso.with(mContext).load(R.drawable.closeeye).into(mEye);
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(mEtPassword);
            isapppwd = false;
        } else {
            // 设置为明文显示
            Picasso.with(mContext).load(R.drawable.eye).into(mEye);
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(mEtPassword);
            isapppwd = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasToolBar(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        MyApplication.getInstance().add(this);
        initView();
    }

    private void initView(){
        //toolbar
        mTool.setTitle("");
        setSupportActionBar(mTool);
        mTool.setNavigationIcon(null);
        //一开始就为秘闻显示
        isAppearPwd();
        if (SharedPreferencesUtil.getInstance(mContext).getString("companyEmail") !=null && !TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("companyEmail"))){
            mEtUserName.setText(SharedPreferencesUtil.getInstance(mContext).getString("companyEmail"));
        }
    }

    private void login(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("companyEmail",mEtUserName.getText().toString());
        map.put("password",mEtPassword.getText().toString());
        map.put("digest", MdTools.sign_digest(map));

        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.LOGIN, map, TAG, new SpotsCallBack<LoginData>(mContext) {
            @Override
            public void onSuccess(Response response, LoginData data) {
                List<String> cookie = response.headers("set-cookie");
                    if (data.getCode().equals("200")){
                        SharedPreferencesUtil.getInstance(mContext).putString("companyEmail",mEtUserName.getText().toString());
                        SharedPreferencesUtil.getInstance(mContext).putString("userid",data.getUserId());
                        startActivity(new Intent(mContext,HomeActivity.class));
                    }else if (data.getCode().equals("110")){
                        goBack(data.getMessage(),mContext);
                    }else {
                        showMessageDialog(data.getMessage(),mContext);
                    }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.login_main_layout;
    }
}
