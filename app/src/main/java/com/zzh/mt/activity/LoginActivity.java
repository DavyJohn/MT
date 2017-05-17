package com.zzh.mt.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.utils.CommonUtil;

import butterknife.BindView;
import butterknife.OnClick;

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
    @OnClick(R.id.login_registered) void registered(){
        startActivity(new Intent(mContext,RegisteredActivity.class));
    }

    @OnClick(R.id.login_forget_password) void forget(){
//        startActivity();
    }
    @OnClick(R.id.login_eye) void appearpwd(){
        isAppearPwd();
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
        getToolBar().setTitle(getString(R.string.login));
        MyApplication.getInstance().add(this);
        setToolbarColor(R.color.transparent);
//        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        //一开始就为秘闻显示
        isAppearPwd();
        //登录 输入框
        Drawable drawableleft = ContextCompat.getDrawable(mContext,R.mipmap.ic_launcher);
        Drawable drawableright = ContextCompat.getDrawable(mContext,R.mipmap.ic_launcher);
        drawableleft.setBounds(0,0,40,40);
        drawableright.setBounds(0,0,40,40);
        mEtPassword.setPadding(30,0,30,0);
        mEtUserName.setPadding(30,0,30,0);
        mEtUserName.setCompoundDrawables(drawableleft,null,null,null);
        mEtPassword.setCompoundDrawables(drawableright,null,null,null);

    }

    @Override
    public int getLayoutId() {
        return R.layout.login_main_layout;
    }
}
