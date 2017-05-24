package com.zzh.mt.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ForgetPassActivity extends BaseActivity {
    @BindView(R.id.forget_et_username)
    EditText mEditAddress;
    @BindView(R.id.forget_toolbar)
    Toolbar mTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasToolBar(false);
        MyApplication.getInstance().add(this);
       initview();
    }

    private void initview(){
        Drawable drawableleft = ContextCompat.getDrawable(mContext,R.drawable.login_username_icon);
        drawableleft.setBounds(0,0,34,36);//长宽
        mEditAddress.setPadding(30,0,30,0);
        mEditAddress.setCompoundDrawables(drawableleft,null,null,null);
        mTool.setTitle("");
        setSupportActionBar(mTool);
        mTool.setNavigationIcon(R.drawable.ic_backback);
        mTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.forget_pass_main_layout;
    }
}
