package com.zzh.mt.activity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
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

    }

    @Override
    public int getLayoutId() {
        return R.layout.registered_mine_layout;
    }
}
