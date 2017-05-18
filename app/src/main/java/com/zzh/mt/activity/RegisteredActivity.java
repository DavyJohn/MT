package com.zzh.mt.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
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

public class RegisteredActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private boolean isapppwd = true;
    @BindView(R.id.registered_et_username)
    EditText mEtUserName;
    @BindView(R.id.registered_et_password)
    EditText mEtPassword;
    @BindView(R.id.registered_eye)
    ImageView mEye;
    @BindView(R.id.registered_toolbar)
    Toolbar mTool;
    @OnClick(R.id.registered_eye) void appearpwd(){
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
        MyApplication.getInstance().add(this);
        hasToolBar(false);
        getToolBar().setTitle(R.string.registered);
        setToolbarColor(R.color.transparent);
        initView();

    }
    private void initView(){
        //toolbar
        mTool.setTitle("");
        setSupportActionBar(mTool);
        mTool.setNavigationIcon(R.drawable.ic_backback);
        mTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退回上一级
                finish();
            }
        });
        //一开始就为秘闻显示
        isAppearPwd();
        //登录 输入框
        Drawable drawableleft = ContextCompat.getDrawable(mContext,R.drawable.login_username_icon);
        Drawable drawableright = ContextCompat.getDrawable(mContext,R.drawable.login_password_icon);
        drawableleft.setBounds(0,0,34,36);//长宽
        drawableright.setBounds(0,0,34,36);//长宽
        mEtPassword.setPadding(30,0,30,0);
        mEtUserName.setPadding(30,0,30,0);
        mEtUserName.setCompoundDrawables(drawableleft,null,null,null);
        mEtPassword.setCompoundDrawables(drawableright,null,null,null);

    }
    @Override
    public int getLayoutId() {
        return R.layout.registered_mine_layout;
    }
}
