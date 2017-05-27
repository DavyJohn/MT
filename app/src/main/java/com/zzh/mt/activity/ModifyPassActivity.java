package com.zzh.mt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ModifyPassActivity extends BaseActivity {
    @BindView(R.id.original_password)
    EditText mOldPassword;
    @BindView(R.id.new_password)
    EditText mNewPass;
    @BindView(R.id.new_password_second)
    EditText mSecNewPass;
    @OnClick(R.id.modify_btn) void change(){
        if (TextUtils.isEmpty(mOldPassword.getText().toString())){
            showMessageDialog("请填写当前登录密码!",mContext);
        }else if (TextUtils.isEmpty(mNewPass.getText().toString())){
            showMessageDialog("请输入新密码!",mContext);
        }else if (TextUtils.isEmpty(mSecNewPass.getText().toString())){
            showMessageDialog("请再次输入新密码!",mContext);
        }else if (!mNewPass.getText().toString().equals(mSecNewPass.getText().toString())){
            showMessageDialog("两次密码输入不一致！",mContext);
        }else {
            finish();
            showToast("修改成功");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("密码更新");

    }

    private void initview(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.modify_pass_main_layout;
    }
}
